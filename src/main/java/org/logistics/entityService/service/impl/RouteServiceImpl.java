package org.logistics.entityService.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.constants.EntityTypes;
import org.logistics.entityService.controller.PlaceController;
import org.logistics.entityService.data.entities.RouteEntity;
import org.logistics.entityService.data.repositories.RouteRepository;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.kafka.Producer;
import org.logistics.entityService.model.data.RouteAttributes;
import org.logistics.entityService.model.data.RouteDetails;
import org.logistics.entityService.model.response.CreatePlaceResponseModel;
import org.logistics.entityService.service.RouteService;
import org.logistics.entityService.shared.RouteDto;
import org.logistics.entityService.shared.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class RouteServiceImpl implements RouteService {

    private RouteRepository routeRepository;
    private PlaceController placeController;
    private MapService mapService;
    private Producer event;
    private Utils utils;
    ModelMapper modelMapper;

    @Autowired
    public RouteServiceImpl(RouteRepository routeRepository, PlaceController placeController, MapService mapService, Producer event, Utils utils) {
        this.utils = utils;
        this.routeRepository = routeRepository;
        this.placeController = placeController;
        this.mapService = mapService;
        this.event = event;
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
    }

    @Override
    public RouteDto createRoute(RouteDto routeDto, String requesterId) {
        routeDto.setRid(EntityTypes.RTE.name() + "-" + utils.getUniqueId());
        validatePlaceDetailsAndPopulateRouteAttributes(routeDto);
        routeDto.setIsActive(true);
        routeDto.setIsDeleted(false);
        routeDto.setCreatedAt(new Date());
        routeDto.setUpdatedAt(new Date());
        routeDto.setCreatedBy(requesterId);
        RouteEntity routeEntity = modelMapper.map(routeDto, RouteEntity.class);
        routeRepository.saveAndFlush(routeEntity);
        event.sendRouteEvent("create_route", routeDto);
        return routeDto;
    }

    @Override
    public List<RouteDto> getAllRoutes(boolean all) {
        List<RouteEntity> post = !all ? (List<RouteEntity>) routeRepository.findAllActiveRoutes() : (List<RouteEntity>) routeRepository.findAllRoutes();
        Type listType = new TypeToken<List<RouteDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public List<RouteDto> getAllRouteBasedOnRid(String rid, boolean all) {
        List<RouteEntity> post = !all ? (List<RouteEntity>) routeRepository.findAllRoutesBasedOnRidAndAreActive(rid) : (List<RouteEntity>) routeRepository.findAllByRid(rid);
        Type listType = new TypeToken<List<RouteDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public RouteDto updateRoute(String rid, RouteDto routeDto, String requesterId) {
        routeDto.setRid(rid);
        routeDto.setUpdatedAt(new Date());
        routeDto.setUpdatedBy(requesterId);
        log.info("check for the presence of route within system with rid : {}", rid);
        List<RouteEntity> routeEntityList = (List<RouteEntity>) routeRepository.findAllActiveRoutesForRid(rid);
        if (routeEntityList.size() == 0)
            throw new EntityServiceException("No routes found to update!!!");
        RouteEntity routeEntity = routeEntityList.get(0);
        validatePlaceDetailsAndPopulateRouteAttributes(routeDto);
        modelMapper.map(routeDto, routeEntity);
        routeRepository.saveAndFlush(routeEntity);
        modelMapper.map(routeEntity, routeDto);
        event.sendRouteEvent("update_route", routeDto);
        return routeDto;
    }

    @Override
    public RouteDto updateRouteStatus(String rid, RouteDto routeDto, String requesterId) {
        List<RouteEntity> routeEntityList = (List<RouteEntity>) routeRepository.findAllByRid(rid);
        if (routeEntityList.size() == 0)
            throw new EntityServiceException("No routes found!!!");
        RouteEntity routeEntity = routeEntityList.get(0);
        routeDto.setUpdatedAt(new Date());
        routeDto.setUpdatedBy(requesterId);
        modelMapper.map(routeDto, routeEntity);
        modelMapper.map(routeEntity, routeDto);
        routeRepository.saveAndFlush(routeEntity);
        event.sendRouteEvent("update_route", routeDto);
        return routeDto;
    }

    @Override
    public RouteDto deleteRoute(RouteDto routeDto, String requesterId) {
        List<RouteEntity> routeEntityList = (List<RouteEntity>) routeRepository.findAllByRid(routeDto.getRid());
        if (routeEntityList.size() == 0)
            throw new EntityServiceException("No routes found!!!");
        RouteEntity routeEntity = routeEntityList.get(0);
        routeDto.setUpdatedAt(new Date());
        routeDto.setUpdatedBy(requesterId);
        routeDto.setIsDeleted(true);
        routeDto.setDeletedAt(new Date());
        modelMapper.map(routeDto, routeEntity);
        modelMapper.map(routeEntity, routeDto);
        routeRepository.saveAndFlush(routeEntity);
        event.sendRouteEvent("delete_route", routeDto);
        return routeDto;
    }

    private void validatePlaceDetailsAndPopulateRouteAttributes(RouteDto routeDto) {
        final List<CreatePlaceResponseModel> originPlace = new ArrayList<CreatePlaceResponseModel>();
        final List<CreatePlaceResponseModel> destinationPlace = new ArrayList<CreatePlaceResponseModel>();
        placeController.getPlaceBasedOnPid(false, "pid", routeDto.getOriginPid()).getBody().getData().forEach(value -> originPlace.add(value));
        placeController.getPlaceBasedOnPid(false, "pid", routeDto.getDestinationPid()).getBody().getData().forEach(value -> destinationPlace.add(value));
        if (!originPlace.get(0).getParentId().equals(routeDto.getParentId()) && !destinationPlace.get(0).getParentId().equals(routeDto.getParentId()))
            throw new EntityServiceException("Origin or Destination doesn't belong to current parent");
        if (routeDto.getOriginPid().equals(routeDto.getDestinationPid()))
            throw new EntityServiceException("Origin and Destination cannot be same");
        RouteDetails routeDetails = mapService.getRouteDetails(originPlace.get(0).getLatitude() + "," + originPlace.get(0).getLongitude(),
                destinationPlace.get(0).getLatitude() + "," + destinationPlace.get(0).getLongitude());
        log.info("Retrieved route Details : {}", routeDetails);
        RouteAttributes routeAttributes = new RouteAttributes();
        routeAttributes.setStandardDistanceInKilometers((int) Math.ceil((double) routeDetails.getRows().get(0).getElements().get(0).getDistance().getValue() / 1000));
        routeAttributes.setStandardTransitTimeInMins((int) Math.ceil((double) routeDetails.getRows().get(0).getElements().get(0).getDuration().getValue() / 60));
        routeDto.setAttributes(routeAttributes);
    }
}

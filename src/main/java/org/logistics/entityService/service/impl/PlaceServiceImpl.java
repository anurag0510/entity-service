package org.logistics.entityService.service.impl;

import org.locationtech.jts.geom.*;
import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.constants.EntityTypes;
import org.logistics.entityService.controller.CompanyController;
import org.logistics.entityService.controller.UserController;
import org.logistics.entityService.data.entities.PlaceEntity;
import org.logistics.entityService.data.repositories.PlaceRepository;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.kafka.Producer;
import org.logistics.entityService.model.data.GeometryData;
import org.logistics.entityService.model.data.GetPlaceDetailsModel;
import org.logistics.entityService.model.data.GoogleAttributes;
import org.logistics.entityService.model.data.PlaceAttributes;
import org.logistics.entityService.service.CompanyService;
import org.logistics.entityService.service.PlaceService;
import org.logistics.entityService.service.UserService;
import org.logistics.entityService.shared.PlaceDto;
import org.logistics.entityService.shared.Utils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.internal.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PlaceServiceImpl implements PlaceService {

    private PlaceRepository placeRepository;
    private UserController userController;
    private CompanyController companyController;
    private MapService mapService;
    private Producer event;
    private Utils utils;
    ModelMapper modelMapper;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository, MapService mapService, UserController userController, CompanyController companyController, Producer event, Utils utils) {
        this.utils = utils;
        this.placeRepository = placeRepository;
        this.mapService = mapService;
        this.userController = userController;
        this.companyController = companyController;
        this.event = event;
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
    }


    @Override
    public PlaceDto createPlace(PlaceDto placeDto, String requesterId) {
        isValidCompany(placeDto.getParentId());
        isValidUser(requesterId);
        placeDto.setPid(EntityTypes.PLC.name() + "-" + utils.getUniqueId());
        placeDto.setIsActive(true);
        placeDto.setCreatedAt(new Date());
        placeDto.setUpdatedAt(new Date());
        placeDto.setCreatedBy(requesterId);
        PlaceEntity placeEntity = modelMapper.map(placeDto, PlaceEntity.class);
        placeEntity.setGooglePlaceId(mapService.getPlaceIdBasedOnLatLong(placeEntity.getLatitude(), placeEntity.getLongitude()).getResults()[0].getPlaceId());
        setPlaceDataBasedOnLatLng(placeEntity);
        placeEntity.setIsDeleted(false);
        modelMapper.map(placeEntity, placeDto);
        placeRepository.saveAndFlush(placeEntity);
        event.sendPlaceEvent("create_place", placeDto);
        return placeDto;
    }

    @Override
    public PlaceDto updatePlace(String pid, PlaceDto placeDto, String requesterId) {
        placeDto.setPid(pid);
        placeDto.setUpdatedAt(new Date());
        placeDto.setUpdatedBy(requesterId);
        log.info("check for the presence of place within system with pid : {}", pid);
        List<PlaceEntity> placeEntityList = (List<PlaceEntity>) placeRepository.findAllActivePlacesForPid(pid);
        if (placeEntityList.size() == 0)
            throw new EntityServiceException("No places found to update!!!");
        PlaceEntity placeEntity = placeEntityList.get(0);
        if (placeDto.getLatitude() == placeEntity.getLatitude() && placeDto.getLongitude() == placeEntity.getLongitude()) {
            log.info("latitude and longitude of update request matches with system values so no need calling of google apis to collect place details");
            modelMapper.map(placeDto, placeEntity);
        } else {
            modelMapper.map(placeDto, placeEntity);
            placeEntity.setGooglePlaceId(mapService.getPlaceIdBasedOnLatLong(placeEntity.getLatitude(), placeEntity.getLongitude()).getResults()[0].getPlaceId());
            setPlaceDataBasedOnLatLng(placeEntity);
        }
        placeRepository.saveAndFlush(placeEntity);
        modelMapper.map(placeEntity, placeDto);
        event.sendPlaceEvent("update_place", placeDto);
        return placeDto;
    }

    @Override
    public List<PlaceDto> getAllPlacesBasedOnPid(String pid, boolean allPlaces) {
        List<PlaceEntity> post = !allPlaces ? (List<PlaceEntity>) placeRepository.findAllPlacesBasedOnPidAndAreActive(pid) : (List<PlaceEntity>) placeRepository.findAllByPid(pid);
        Type listType = new TypeToken<List<PlaceDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public List<PlaceDto> getAllPlaces(boolean allPlaces) {
        List<PlaceEntity> post = !allPlaces ? (List<PlaceEntity>) placeRepository.findAllActivePlaces() : (List<PlaceEntity>) placeRepository.findAllPlaces();
        Type listType = new TypeToken<List<PlaceDto>>() {
        }.getType();
        return modelMapper.map(post, listType);
    }

    @Override
    public PlaceDto updatePlaceStatus(String placeId, PlaceDto placeDto, String requesterId) {
        List<PlaceEntity> placeEntityList = (List<PlaceEntity>) placeRepository.findAllByPid(placeId);
        if (placeEntityList.size() == 0)
            throw new EntityServiceException("No places found!!!");
        PlaceEntity placeEntity = placeEntityList.get(0);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        placeDto.setUpdatedAt(new Date());
        placeDto.setUpdatedBy(requesterId);
        modelMapper.map(placeDto, placeEntity);
        modelMapper.map(placeEntity, placeDto);
        placeRepository.saveAndFlush(placeEntity);
        event.sendPlaceEvent("update_place", placeDto);
        return placeDto;
    }

    @Override
    public PlaceDto deletePlace(PlaceDto placeDto, String requesterId) {
        List<PlaceEntity> placeEntityList = (List<PlaceEntity>) placeRepository.findAllByPid(placeDto.getPid());
        if (placeEntityList.size() == 0)
            throw new EntityServiceException("No places found!!!");
        PlaceEntity placeEntity = placeEntityList.get(0);
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
        placeDto.setUpdatedAt(new Date());
        placeDto.setUpdatedBy(requesterId);
        placeDto.setIsDeleted(true);
        placeDto.setDeletedAt(new Date());
        modelMapper.map(placeDto, placeEntity);
        modelMapper.map(placeEntity, placeDto);
        placeRepository.saveAndFlush(placeEntity);
        event.sendPlaceEvent("delete_place", placeDto);
        return placeDto;
    }

    public void setPlaceDataBasedOnLatLng(PlaceEntity placeEntity) {
        log.info("check whether following google_place_id is already present in system or not");
        List<PlaceEntity> placeCriteriaList = (List<PlaceEntity>) placeRepository.findByGooglePlaceId(placeEntity.getGooglePlaceId());
        if (placeCriteriaList.size() != 0 && !placeCriteriaList.get(0).getPid().equals(placeEntity.getPid())) {
            log.info("Place found with matching google_place_id : {} in system", placeEntity.getGooglePlaceId());
            placeEntity.setRegion(placeCriteriaList.get(0).getRegion());
            placeEntity.setCountry(placeCriteriaList.get(0).getCountry());
            placeEntity.setLocality(placeCriteriaList.get(0).getLocality());
            placeEntity.setPostalCode(placeCriteriaList.get(0).getPostalCode());
            placeEntity.setShape(placeCriteriaList.get(0).getShape());
            placeEntity.setGeometry(placeCriteriaList.get(0).getGeometry());
            placeEntity.setAttributes(placeCriteriaList.get(0).getAttributes());
            placeEntity.setFormattedAddress(placeCriteriaList.get(0).getFormattedAddress());
            placeEntity.setRegion(placeCriteriaList.get(0).getRegion());
        } else {
            log.info("Place not found with matching google_place_id : {} in system", placeEntity.getGooglePlaceId());
            GetPlaceDetailsModel placeDetailsModel = mapService.getPlaceDetailsBasedOnId(placeEntity.getGooglePlaceId());
            modelMapper.map(placeDetailsModel.getResult(), placeEntity);
            GoogleAttributes googleAttributes = modelMapper.map(placeDetailsModel.getResult(), GoogleAttributes.class);
            placeEntity.setAttributes(new PlaceAttributes(googleAttributes));
            Point point = getPoint(placeDetailsModel.getResult().getGeometry().getLocation().getLng(), placeDetailsModel.getResult().getGeometry().getLocation().getLat());
            point.setSRID(4326);
            placeEntity.setGeometry(point);
            placeEntity.setShape(getShape(placeDetailsModel.getResult().getGeometry()));
            if (placeDetailsModel.getResult().getAdrAddress() != null)
                populateLocalityRegionPostalCodeAndCountry(placeDetailsModel.getResult().getAdrAddress(), placeEntity);
        }
    }

    private void populateLocalityRegionPostalCodeAndCountry(String adrAddress, PlaceEntity placeEntity) {
        placeEntity.setRegion(utils.getValueForSpanClass(adrAddress, "region"));
        placeEntity.setLocality(utils.getValueForSpanClass(adrAddress, "locality"));
        placeEntity.setCountry(utils.getValueForSpanClass(adrAddress, "country-name"));
        placeEntity.setPostalCode(utils.getValueForSpanClass(adrAddress, "postal-code"));
    }

    public Polygon getShape(GeometryData geometryData) {
        GeometryFactory geomFactory = new GeometryFactory();
        Coordinate[] coords = new Coordinate[]{
                getCoordinate(geometryData.getViewport().getNortheast().getLng(), geometryData.getViewport().getNortheast().getLat()),
                getCoordinate(geometryData.getViewport().getSouthwest().getLng(), geometryData.getViewport().getNortheast().getLat()),
                getCoordinate(geometryData.getViewport().getSouthwest().getLng(), geometryData.getViewport().getSouthwest().getLat()),
                getCoordinate(geometryData.getViewport().getNortheast().getLng(), geometryData.getViewport().getSouthwest().getLat()),
                getCoordinate(geometryData.getViewport().getNortheast().getLng(), geometryData.getViewport().getNortheast().getLat())
        };
        return geomFactory.createPolygon(coords);
    }

    public Point getPoint(Float lng, Float lat) {
        GeometryFactory geomFactory = new GeometryFactory();
        return geomFactory.createPoint(new Coordinate(lng, lat));
    }

    public Coordinate getCoordinate(Float lng, Float lat) {
        return new Coordinate(lng, lat);
    }

    private boolean isValidUser(String userId) {
        if (userController.getUser(false, "uid", userId).getBody().getData().spliterator().getExactSizeIfKnown() == 0)
            throw new EntityServiceException("No such user within system with id : " + userId);
        return true;
    }

    private boolean isValidCompany(String companyId) {
        if (companyId == null)
            return true;
        if (companyController.getCompanyBasedOnCid(false, "cid", companyId).getBody().getData().spliterator().getExactSizeIfKnown() == 0)
            throw new EntityServiceException("No such company within system with id : " + companyId);
        return true;
    }
}

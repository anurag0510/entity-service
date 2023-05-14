package org.logistics.entityService.controller;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.model.request.CreateRouteRequestModel;
import org.logistics.entityService.model.request.UpdateEntityActiveStatusRequestModel;
import org.logistics.entityService.model.response.*;
import org.logistics.entityService.service.RouteService;
import org.logistics.entityService.shared.RouteDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/route")
public class RouteController {

    RouteService routeService;
    ModelMapper modelMapper;

    @Autowired
    public RouteController(RouteService routeService) {
        this.routeService = routeService;
        this.modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setSkipNullEnabled(true);
    }

    @PostMapping
    public ResponseEntity<CreateRouteResponseModel> createRoute(
            @Valid @RequestBody CreateRouteRequestModel routeDetails,
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId
    ) {
        log.info("Route details to be created within system : {}", routeDetails);
        RouteDto routeDto = modelMapper.map(routeDetails, RouteDto.class);
        CreateRouteResponseModel createRouteResponseModel = modelMapper.map(routeService.createRoute(routeDto, requesterId), CreateRouteResponseModel.class);
        log.info("Returning Details : {}", createRouteResponseModel);
        return new ResponseEntity<>(createRouteResponseModel, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<GetAllRouteResponseModel> getAllRoute(@RequestParam(value = "all", defaultValue = "false", required = false) boolean all) {
        log.info("Request received to fetch all routes in system");
        GetAllRouteResponseModel returnValue = new GetAllRouteResponseModel();
        returnValue.setSuccess(true);
        Type listType = new TypeToken<List<CreateRouteResponseModel>>() {
        }.getType();
        List<CreateRouteResponseModel> routesList = modelMapper.map(routeService.getAllRoutes(all), listType);
        if (routesList.size() == 0)
            throw new EntityServiceException("No routes found!!!");
        returnValue.setData(routesList);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @GetMapping(value = "/{filter}/{rid}")
    public ResponseEntity<GetAllRouteResponseModel> getRouteBasedOnRid(@RequestParam(value = "all", defaultValue = "false", required = false) boolean all,
                                                                        @PathVariable(value = "filter", required = true) String filter,
                                                                        @PathVariable(value = "rid", required = true) String rid) {
        log.info("Request received to fetch route with rid : {} ", rid);
        if(!filter.matches("(?i)rid"))
            throw new EntityServiceException("Only allowed to filter via : rid");
        GetAllRouteResponseModel returnValue = new GetAllRouteResponseModel();
        returnValue.setSuccess(true);
        Type listType = new TypeToken<List<CreateRouteResponseModel>>() {
        }.getType();
        List<CreateRouteResponseModel> routesList = modelMapper.map(routeService.getAllRouteBasedOnRid(rid, all), listType);
        if (routesList.size() == 0)
            throw new EntityServiceException("No routes found!!!");
        returnValue.setData(routesList);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PutMapping(value = "/{filter}/{rid}")
    public ResponseEntity<CreateRouteResponseModel> updateRoute(@Valid @RequestBody UpdateRouteRequestModel routeDetails,
                                                                @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
                                                                @PathVariable(value = "filter", required = true) String filter,
                                                                @PathVariable(value = "rid", required = true) String rid) {
        log.info("Request received to update route with details : {} ", routeDetails.toString());
        if(!filter.matches("(?i)rid"))
            throw new EntityServiceException("Only allowed to filter via : rid");
        RouteDto routeDto = modelMapper.map(routeDetails, RouteDto.class);
        CreateRouteResponseModel updatedRouteResponseModel = modelMapper.map(routeService.updateRoute(rid, routeDto, requesterId), CreateRouteResponseModel.class);
        log.info("Returning Details : {}", updatedRouteResponseModel);
        return new ResponseEntity<>(updatedRouteResponseModel, HttpStatus.OK);
    }

    @PatchMapping(value = "/{filter}/{rid}")
    public ResponseEntity<CreateRouteResponseModel> changeRouteStatus(
            @Valid @RequestBody UpdateEntityActiveStatusRequestModel statusDetails,
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "rid", required = true) String rid
    ) {
        log.info("Request to change route status received with details : {}", statusDetails);
        if(!filter.matches("(?i)rid"))
            throw new EntityServiceException("Only allowed to filter via : rid");
        RouteDto routeDto = modelMapper.map(statusDetails, RouteDto.class);
        CreateRouteResponseModel updatedRouteResponseModel = modelMapper.map(routeService.updateRouteStatus(rid, routeDto, requesterId), CreateRouteResponseModel.class);
        return new ResponseEntity<>(updatedRouteResponseModel, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{filter}/{rid}")
    public ResponseEntity<CreateRouteResponseModel> deleteRoute(
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "rid", required = true) String rid
    ) {
        log.info("Request to delete route received for rid : {}", rid);
        if(!filter.matches("(?i)rid"))
            throw new EntityServiceException("Only allowed to filter via : rid");
        RouteDto routeDto = new RouteDto();
        routeDto.setRid(rid);
        CreateRouteResponseModel deletedRouteResponseModel = modelMapper.map(routeService.deleteRoute(routeDto, requesterId), CreateRouteResponseModel.class);
        return new ResponseEntity<>(deletedRouteResponseModel, HttpStatus.OK);
    }

}

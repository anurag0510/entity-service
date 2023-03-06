package org.logistics.entityService.controller;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.exceptions.EntityServiceException;
import org.logistics.entityService.model.request.CreatePlaceRequestModel;
import org.logistics.entityService.model.request.UpdateEntityActiveStatusRequestModel;
import org.logistics.entityService.model.request.UpdatePlaceRequestModel;
import org.logistics.entityService.model.response.*;
import org.logistics.entityService.service.PlaceService;
import org.logistics.entityService.shared.PlaceDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.lang.reflect.Type;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("api/v1/place")
public class PlaceController {

    @Autowired
    PlaceService placeService;

    @PostMapping
    public ResponseEntity<CreatePlaceResponseModel> createPlace(@Valid @RequestBody CreatePlaceRequestModel placeDetails,
                                                               @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId) {
        log.info("Request received to create place with details : {} ", placeDetails.toString());
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PlaceDto placeDto = modelMapper.map(placeDetails, PlaceDto.class);
        CreatePlaceResponseModel createPlaceResponseModel = modelMapper.map(placeService.createPlace(placeDto, requesterId), CreatePlaceResponseModel.class);
        log.info("Returning Details : {}", createPlaceResponseModel);
        return new ResponseEntity<>(createPlaceResponseModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{filter}/{pid}")
    public ResponseEntity<CreatePlaceResponseModel> updatePlace(@Valid @RequestBody UpdatePlaceRequestModel placeDetails,
                                                               @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
                                                               @PathVariable(value = "filter", required = true) String filter,
                                                               @PathVariable(value = "pid", required = true) String pid) {
        log.info("Request received to update place with details : {} ", placeDetails.toString());
        if(!filter.matches("(?i)pid"))
            throw new EntityServiceException("Only allowed to filter via : pid");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PlaceDto placeDto = modelMapper.map(placeDetails, PlaceDto.class);
        CreatePlaceResponseModel updatedPlaceResponseModel = modelMapper.map(placeService.updatePlace(pid, placeDto, requesterId), CreatePlaceResponseModel.class);
        log.info("Returning Details : {}", updatedPlaceResponseModel);
        return new ResponseEntity<>(updatedPlaceResponseModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{filter}/{pid}")
    public ResponseEntity<GetAllPlacesResponseModel> getPlaceBasedOnPid(@RequestParam(value = "all", defaultValue = "false", required = false) boolean all,
                                                                        @PathVariable(value = "filter", required = true) String filter,
                                                                        @PathVariable(value = "pid", required = true) String pid) {
        log.info("Request received to fetch place with pid : {} ", pid);
        if(!filter.matches("(?i)pid"))
            throw new EntityServiceException("Only allowed to filter via : pid");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        GetAllPlacesResponseModel returnValue = new GetAllPlacesResponseModel();
        returnValue.setSuccess(true);
        Type listType = new TypeToken<List<CreatePlaceResponseModel>>() {
        }.getType();
        List<CreatePlaceResponseModel> placesList = modelMapper.map(placeService.getAllPlacesBasedOnPid(pid, all), listType);
        if (placesList.size() == 0)
            throw new EntityServiceException("No places found!!!");
        returnValue.setData(placesList);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<GetAllPlacesResponseModel> getAllPlaces(@RequestParam(value = "all", defaultValue = "false", required = false) boolean all) {
        log.info("Request received to fetch all places in system");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        GetAllPlacesResponseModel returnValue = new GetAllPlacesResponseModel();
        returnValue.setSuccess(true);
        Type listType = new TypeToken<List<CreatePlaceResponseModel>>() {
        }.getType();
        List<CreatePlaceResponseModel> placesList = modelMapper.map(placeService.getAllPlaces(all), listType);
        if (placesList.size() == 0)
            throw new EntityServiceException("No places found!!!");
        returnValue.setData(placesList);
        return new ResponseEntity<>(returnValue, HttpStatus.OK);
    }

    @PatchMapping(value = "/{filter}/{pid}")
    public ResponseEntity<CreatePlaceResponseModel> changePlaceStatus(
            @Valid @RequestBody UpdateEntityActiveStatusRequestModel statusDetails,
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "pid", required = true) String pid
    ) {
        log.info("Request to change place status received with details : {}", statusDetails);
        if(!filter.matches("(?i)pid"))
            throw new EntityServiceException("Only allowed to filter via : pid");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PlaceDto placeDto = modelMapper.map(statusDetails, PlaceDto.class);
        CreatePlaceResponseModel updatedPlaceResponseModel = modelMapper.map(placeService.updatePlaceStatus(pid, placeDto, requesterId), CreatePlaceResponseModel.class);
        return new ResponseEntity<>(updatedPlaceResponseModel, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{filter}/{pid}")
    public ResponseEntity<CreatePlaceResponseModel> deletePlace(
            @RequestHeader(name = "x-requester-id", required = true) @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}") String requesterId,
            @PathVariable(value = "filter", required = true) String filter,
            @PathVariable(value = "pid", required = true) String pid
    ) {
        log.info("Request to delete place received for pid : {}", pid);
        if(!filter.matches("(?i)pid"))
            throw new EntityServiceException("Only allowed to filter via : pid");
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        PlaceDto placeDto = new PlaceDto();
        placeDto.setPid(pid);
        CreatePlaceResponseModel deletedPlaceResponseModel = modelMapper.map(placeService.deletePlace(placeDto, requesterId), CreatePlaceResponseModel.class);
        return new ResponseEntity<>(deletedPlaceResponseModel, HttpStatus.OK);
    }

}

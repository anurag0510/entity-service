package org.logistics.entityService.service;

import org.logistics.entityService.model.request.ValidateUserPasswordRequestModel;
import org.logistics.entityService.shared.CompanyDto;
import org.logistics.entityService.shared.PlaceDto;
import org.logistics.entityService.shared.UserDto;

import java.util.List;

public interface PlaceService {

    PlaceDto createPlace(PlaceDto placeDto, String requesterId);

    PlaceDto updatePlace(String pid, PlaceDto placeDto, String requesterId);

    List<PlaceDto> getAllPlacesBasedOnPid(String pid, boolean allPlaces);

    List<PlaceDto> getAllPlaces(boolean allPlaces);

    PlaceDto updatePlaceStatus(String placeId, PlaceDto placeDto, String requesterId);

    PlaceDto deletePlace(PlaceDto placeDto, String requesterId);

}
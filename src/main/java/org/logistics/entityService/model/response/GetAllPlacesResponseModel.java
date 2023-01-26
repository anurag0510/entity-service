package org.logistics.entityService.model.response;

import lombok.Data;

@Data
public class GetAllPlacesResponseModel {

    private boolean success = true;
    private Iterable<CreatePlaceResponseModel> data;
}

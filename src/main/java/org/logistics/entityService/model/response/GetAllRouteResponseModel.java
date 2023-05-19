package org.logistics.entityService.model.response;

import lombok.Data;

@Data
public class GetAllRouteResponseModel {

    private boolean success = true;
    private Iterable<CreateRouteResponseModel> data;
}

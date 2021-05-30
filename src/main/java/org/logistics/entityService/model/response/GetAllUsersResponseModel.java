package org.logistics.entityService.model.response;

import lombok.Data;

@Data
public class GetAllUsersResponseModel {

    private boolean success = true;
    private Iterable<CreateUserResponseModel> data;
}

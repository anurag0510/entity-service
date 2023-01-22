package org.logistics.entityService.model.response;

import lombok.Data;

@Data
public class GetAllCompanyResponseModel {

    private boolean success = true;
    private Iterable<CreateCompanyResponseModel> data;
}

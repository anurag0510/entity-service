package org.logistics.entityService.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserPasswordRequestModel {

    @NotNull(message = "oldPassword cannot be empty and is required field.")
    @Size(min = 3, max = 128, message = "password can have length between 3 and 128.")
    private String oldPassword;

    @NotNull(message = "newPassword cannot be empty and is required field.")
    @Size(min = 3, max = 128, message = "password can have length between 3 and 128.")
    private String newPassword;
}

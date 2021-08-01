package org.logistics.entityService.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.logistics.entityService.model.data.Place;
import org.logistics.entityService.validation.annotations.CompanyType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCompanyRequestModel {

    @NotNull(message = "short_code cannot be empty and is required field.")
    @Size(min = 3, max = 50, message = "short_code can have length between 3 and 50.")
    @JsonProperty("short_code")
    private String shortCode;

    @NotNull(message = "name cannot be empty and is required field.")
    @Size(min = 3, max = 128, message = "name can have length between 3 and 128.")
    @JsonProperty("name")
    private String name;

    @Size(min = 3, max = 50, message = "gstin can have length between 3 and 50.")
    @JsonProperty("gstin")
    private String gstin;

    @Size(min = 3, max = 50, message = "tin can have length between 3 and 50.")
    @JsonProperty("tin")
    private String tin;

    @Size(min = 3, max = 50, message = "cin can have length between 3 and 50.")
    @JsonProperty("cin")
    private String cin;

    @Size(min = 3, max = 50, message = "pan can have length between 3 and 50.")
    @JsonProperty("pan")
    private String pan;

    @Size(min = 3, max = 50, message = "tan can have length between 3 and 50.")
    @JsonProperty("tan")
    private String tan;

    @NotNull(message = "types for the company is required.")
    @CompanyType
    private String[] types;

    @Pattern(regexp = "USR-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match user id pattern.")
    @JsonProperty("contact_user_id")
    private String contactUserId;

    private CreatePlaceRequestModel place;

    @JsonProperty("head_office")
    private CreatePlaceRequestModel headOffice;

    @NotNull(message = "contact_number cannot be empty and is required field.")
    @Size(min = 10, max = 10, message = "contact_number can have length of 10.")
    @JsonProperty("contact_number")
    private String contactNumber;
}

package org.logistics.entityService.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateCompanyResponseModel {

    String cid;
    String name;
    String gstin;
    String tin;
    String tan;
    String cin;
    String pan;
    String[] types;
    @JsonProperty("short_code")
    String shortCode;
    @JsonProperty("contact_number")
    String contactNumber;
    @JsonProperty("contact_user_id")
    String contactUserId;
    @JsonProperty("place_id")
    String placeId;
    @JsonProperty("head_office_id")
    String headOfficeId;
    @JsonProperty("is_active")
    Boolean isActive;
}

package org.logistics.entityService.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponseModel {

    private String uid;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("country_code")
    private String countryCode;

    @JsonProperty("mobile_number")
    private String mobileNumber;

    private String address;

    private String country;

    private String city;

    @JsonProperty("is_active")
    private boolean active;

    @JsonProperty("is_deleted")
    private boolean deleted;
}

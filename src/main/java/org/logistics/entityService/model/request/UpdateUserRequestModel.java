package org.logistics.entityService.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserRequestModel {

    @Size(min = 3, max = 50, message = "userName can have length between 3 and 50.")
    @JsonProperty("user_name")
    private String userName;

    @Size(min = 3, max = 128, message = "firstName can have length between 3 and 128.")
    @JsonProperty("first_name")
    private String firstName;

    @Size(min = 3, max = 128, message = "lastName can have length between 3 and 128.")
    @JsonProperty("last_name")
    private String lastName;

    @Email
    @Size(min = 3, max = 128, message = "emailAddress can have length between 3 and 128.")
    @JsonProperty("email_address")
    private String emailAddress;

    @Size(min = 1, max = 3, message = "countryCode can have length between 1 and 3.")
    @JsonProperty("country_code")
    private String countryCode;

    @Size(min = 10, max = 10, message = "mobileNumber can have length of 10.")
    @JsonProperty("mobile_number")
    private String mobileNumber;

    @Size(min = 1, max = 100, message = "country can have length between 1 and 100.")
    private String country;

    @Size(min = 1, max = 100, message = "city can have length between 1 and 100.")
    private String city;

    @Size(min = 1, max = 255, message = "address can have length between 1 and 255.")
    private String address;
}

package org.logistics.entityService.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @JsonProperty("uid")
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
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    private String address;
    private String country;
    private String city;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("is_active")
    private boolean active;
    @JsonProperty("is_deleted")
    private boolean deleted;
    @JsonProperty("deleted_at")
    private Date deletedAt;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("updated_by")
    private String updatedBy;

    public boolean getActive() {
        return active;
    }
}

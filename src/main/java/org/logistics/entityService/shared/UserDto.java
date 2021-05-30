package org.logistics.entityService.shared;

import lombok.Data;

import java.util.Date;

@Data
public class UserDto {

    private String uid;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String countryCode;
    private String mobileNumber;
    private String password;
    private String salt;
    private String address;
    private String country;
    private String city;
    private Date createdAt;
    private Date updatedAt;
    private boolean active;
    private boolean deleted;
    private Date deletedAt;

    public boolean getActive() {
        return active;
    }
}

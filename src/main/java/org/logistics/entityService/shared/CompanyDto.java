package org.logistics.entityService.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.logistics.entityService.model.request.CreatePlaceRequestModel;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDto {

    private String cid;
    @JsonProperty("short_code")
    private String shortCode;
    private String name;
    private String gstin;
    private String tan;
    private String tin;
    private String cin;
    private String pan;
    private String[] types;
    @JsonProperty("contact_number")
    private String contactNumber;
    @JsonProperty("contact_user_id")
    private String contactUserId;
    @JsonProperty("place_id")
    private String placeId;
    @JsonProperty("head_office_id")
    private String headOfficeId;
    @JsonProperty("created_at")
    private Date createdAt;
    @JsonProperty("updated_at")
    private Date updatedAt;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
    @JsonProperty("deleted_at")
    private Date deletedAt;
    @JsonProperty("created_by")
    private String createdBy;
    @JsonProperty("updated_by")
    private String updatedBy;
    @JsonIgnore
    private CreatePlaceRequestModel place;
    @JsonIgnore
    private CreatePlaceRequestModel headOffice;
}

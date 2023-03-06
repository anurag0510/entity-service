package org.logistics.entityService.shared;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.logistics.entityService.model.data.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDto {

    private String pid;
    @JsonProperty("short_code")
    private String shortCode;
    private String label;
    private String address;
    @JsonProperty("lat")
    public Float latitude;
    @JsonProperty("lng")
    public Float longitude;
    @JsonProperty("google_place_id")
    public String googlePlaceId;
    @JsonProperty("formatted_address")
    public String formattedAddress;
    @JsonSerialize(using = PointToJsonSerializer.class)
    public Point geometry;
    @JsonProperty("parent_id")
    public String parentId;
    @JsonSerialize(using = PolygonToJsonSerializer.class)
    public Polygon shape;
    public PlaceAttributes attributes;
    public String region;
    public String locality;
    public String country;
    @JsonProperty("postal_code")
    public String postalCode;
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
}

package org.logistics.entityService.model.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.logistics.entityService.model.data.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePlaceResponseModel {

    private String pid;

    @JsonProperty("short_code")
    private String shortCode;

    @JsonProperty("label")
    private String label;

    @JsonProperty("address")
    private String address;

    @JsonProperty("latitude")
    public Float latitude;

    @JsonProperty("longitude")
    public Float longitude;

    @JsonProperty("google_place_id")
    public String googlePlaceId;

    @JsonProperty("formatted_address")
    public String formattedAddress;

    @JsonProperty("parent_id")
    public String parentId;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    @JsonProperty("attributes")
    PlaceAttributes attributes;

    @JsonProperty("region")
    public String region;

    @JsonProperty("locality")
    public String locality;

    @JsonProperty("country")
    public String country;

    @JsonProperty("postal_code")
    public String postalCode;

    @JsonSerialize(using = PointToJsonSerializer.class)
    public Point geometry;

    @JsonSerialize(using = PolygonToJsonSerializer.class)
    public Polygon shape;
}

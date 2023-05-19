package org.logistics.entityService.shared;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.logistics.entityService.model.data.PlaceAttributes;
import org.logistics.entityService.model.data.PointToJsonSerializer;
import org.logistics.entityService.model.data.PolygonToJsonSerializer;
import org.logistics.entityService.model.data.RouteAttributes;

import javax.persistence.Column;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {

    private String rid;
    @JsonProperty("short_code")
    private String shortCode;
    private String label;
    @JsonProperty("parent_id")
    public String parentId;
    public RouteAttributes attributes;
    @JsonProperty("origin_pid")
    private String originPid;
    @JsonProperty("destination_pid")
    private String destinationPid;
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

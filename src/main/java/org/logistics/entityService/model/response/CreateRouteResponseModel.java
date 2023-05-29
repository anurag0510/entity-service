package org.logistics.entityService.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.logistics.entityService.model.data.RouteAttributes;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRouteResponseModel {

    public String rid;

    @JsonProperty("short_code")
    public String shortCode;

    public String label;

    @JsonProperty("parent_id")
    public String parentId;

    public RouteAttributes attributes;

    @JsonProperty("origin_pid")
    public String originPid;

    @JsonProperty("destination_pid")
    public String destinationPid;

    @JsonProperty("is_active")
    public Boolean isActive;

    @JsonProperty("is_deleted")
    public Boolean isDeleted;
}

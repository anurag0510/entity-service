package org.logistics.entityService.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteDistanceAndTime {

    RouteDistance distance;
    RouteDuration duration;
    String status;
}

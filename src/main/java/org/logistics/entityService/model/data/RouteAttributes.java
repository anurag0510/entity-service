package org.logistics.entityService.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteAttributes {

    @JsonProperty("standard_transit_time_in_mins")
    Integer standardTransitTimeInMins;

    @JsonProperty("standard_distance_in_kilometers")
    Integer standardDistanceInKilometers;

}

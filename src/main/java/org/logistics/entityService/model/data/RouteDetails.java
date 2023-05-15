package org.logistics.entityService.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteDetails {

    @JsonProperty("destination_addresses")
    String[] destination_addresses;

    @JsonProperty("origin_addresses")
    String[] origin_addresses;

    List<RouteData> rows;
}

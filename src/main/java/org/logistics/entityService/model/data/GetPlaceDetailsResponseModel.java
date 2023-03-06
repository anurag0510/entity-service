package org.logistics.entityService.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class GetPlaceDetailsResponseModel {

    @JsonProperty("adr_address")
    String adrAddress;

    @JsonProperty("formatted_address")
    String formattedAddress;

    @JsonProperty("geometry")
    GeometryData geometry;

    @JsonProperty("place_id")
    String placeId;

}

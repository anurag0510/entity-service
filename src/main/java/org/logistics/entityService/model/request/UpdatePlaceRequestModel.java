package org.logistics.entityService.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdatePlaceRequestModel {

    @NotNull(message = "label cannot be empty and is required field.")
    @Size(min = 3, max = 128, message = "label can have length between 3 and 128.")
    @JsonProperty("label")
    private String label;

    @NotNull(message = "address cannot be empty and is required field.")
    @Size(min = 1, max = 255, message = "address can have length between 1 and 255.")
    @JsonProperty("address")
    private String address;

    @NotNull(message = "latitude cannot be empty and is required field.")
    @JsonProperty("latitude")
    public Float latitude;

    @NotNull(message = "longitude cannot be empty and is required field.")
    @JsonProperty("longitude")
    public Float longitude;

    @Pattern(regexp = "COM-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match company id pattern.")
    @JsonProperty("parent_id")
    public String parentId;
}

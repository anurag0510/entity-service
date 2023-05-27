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
public class CreateRouteRequestModel {

    @NotNull(message = "short_code cannot be empty and is required field.")
    @Size(min = 3, max = 50, message = "short_code can have length between 3 and 50.")
    @JsonProperty("short_code")
    private String shortCode;

    @NotNull(message = "label cannot be empty and is required field.")
    @Size(min = 3, max = 128, message = "label can have length between 3 and 128.")
    @JsonProperty("label")
    private String label;

    @NotNull(message = "parent_id cannot be empty and is required field.")
    @Pattern(regexp = "COM-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match company id pattern.")
    @JsonProperty("parent_id")
    public String parentId;

    @NotNull(message = "origin_pid cannot be empty and is required field.")
    @Pattern(regexp = "PLC-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match company id pattern.")
    @JsonProperty("origin_pid")
    public String originPid;

    @NotNull(message = "destination_pid cannot be empty and is required field.")
    @Pattern(regexp = "PLC-[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}", message = "Failed to match company id pattern.")
    @JsonProperty("destination_pid")
    public String destinationPid;
}

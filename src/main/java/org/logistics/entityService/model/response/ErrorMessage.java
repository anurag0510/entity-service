package org.logistics.entityService.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorMessage {
    private boolean success = false;

    @JsonProperty("status_code")
    private int statusCode;

    private String error;

    private Object details;
}
package org.logistics.entityService.model.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteDuration {

    public String text;
    public Integer value;
}

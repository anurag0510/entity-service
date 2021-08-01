package org.logistics.entityService.kafka.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.logistics.entityService.shared.PlaceDto;

@Data
public class EntityEventModel {

    @JsonProperty("event_type")
    private String eventType;

    private Long timestamp = System.currentTimeMillis();

    @JsonProperty("message_body")
    private Object messageBody;
}

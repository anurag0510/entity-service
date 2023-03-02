package org.logistics.entityService.kafka;

import lombok.extern.slf4j.Slf4j;
import org.logistics.entityService.kafka.model.EntityEventModel;
import org.logistics.entityService.shared.CompanyDto;
import org.logistics.entityService.shared.PlaceDto;
import org.logistics.entityService.shared.RouteDto;
import org.logistics.entityService.shared.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Producer {

    @Value("${kafka.producer.topic}")
    private String TOPIC_NAME;

    @Autowired
    private KafkaTemplate<String, EntityEventModel> kafkaTemplate;

    public void sendUserEvent(String eventType, UserDto userDto) {
        EntityEventModel entityEvent = new EntityEventModel();
        entityEvent.setEventType(eventType);
        entityEvent.setMessageBody(userDto);
        kafkaTemplate.send(TOPIC_NAME, userDto.getUid().substring(4), entityEvent);
    }

    public void sendCompanyEvent(String eventType, CompanyDto companyDto) {
        EntityEventModel entityEvent = new EntityEventModel();
        entityEvent.setEventType(eventType);
        entityEvent.setMessageBody(companyDto);
        kafkaTemplate.send(TOPIC_NAME, companyDto.getCid().substring(4), entityEvent);
    }

    public void sendPlaceEvent(String eventType, PlaceDto placeDto) {
        EntityEventModel entityEvent = new EntityEventModel();
        entityEvent.setEventType(eventType);
        entityEvent.setMessageBody(placeDto);
        kafkaTemplate.send(TOPIC_NAME, placeDto.getParentId().substring(4), entityEvent);
    }

    public void sendRouteEvent(String eventType, RouteDto routeDto) {
        EntityEventModel entityEvent = new EntityEventModel();
        entityEvent.setEventType(eventType);
        entityEvent.setMessageBody(routeDto);
        kafkaTemplate.send(TOPIC_NAME, routeDto.getRid().substring(4), entityEvent);
    }

}

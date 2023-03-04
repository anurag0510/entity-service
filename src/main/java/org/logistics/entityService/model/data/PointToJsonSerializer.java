package org.logistics.entityService.model.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;
import org.locationtech.jts.geom.Point;

import java.io.IOException;
import java.util.Map;

public class PointToJsonSerializer extends JsonSerializer<Point> {

    @Override
    public void serialize(Point value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException,
            JsonProcessingException {
        try
        {
            if(value != null) {
                jgen.writeStartObject();
                jgen.writeStringField("type", "Point");
                jgen.writeFieldName("coordinates");
                jgen.writeStartArray();
                    jgen.writeNumber(value.getY());
                    jgen.writeNumber(value.getX());
                jgen.writeEndArray();
                jgen.writeEndObject();

            }
        }
        catch(Exception e) {}
    }
}

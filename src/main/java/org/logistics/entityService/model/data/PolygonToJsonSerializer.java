package org.logistics.entityService.model.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

import java.io.IOException;

public class PolygonToJsonSerializer extends JsonSerializer<Polygon> {

    @Override
    public void serialize(Polygon value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException,
            JsonProcessingException {
        try
        {
            if(value != null) {
                Coordinate[] coordinates = value.getCoordinates();
                jgen.writeStartObject();
                jgen.writeStringField("type", "Polygon");
                jgen.writeFieldName("coordinates");
                jgen.writeStartArray();
                jgen.writeStartArray();
                for(Coordinate coordinate: coordinates) {
                    jgen.writeStartArray();
                    jgen.writeNumber(coordinate.getY());
                    jgen.writeNumber(coordinate.getX());
                    jgen.writeEndArray();
                }
                jgen.writeEndArray();
                jgen.writeEndArray();
                jgen.writeEndObject();
            }
        }
        catch(Exception e) {}
    }

}

package org.logistics.entityService.model.data;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.locationtech.jts.geom.*;

import java.io.IOException;

public class JsonToPolygonDeserializer extends JsonDeserializer<Polygon> {

    private final static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 26910);

    @Override
    public Polygon deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        try {
            String text = jp.getText();
            if(text == null || text.length() <= 0)
                return null;

            String[] coordinates = text.replaceFirst("POINT ?\\(", "").replaceFirst("\\)", "").split(" ");
            double lat = Double.parseDouble(coordinates[0]);
            double lon = Double.parseDouble(coordinates[1]);

            Polygon polygon = geometryFactory.createPolygon();
            return polygon;
        }
        catch(Exception e){
            return null;
        }
    }

}

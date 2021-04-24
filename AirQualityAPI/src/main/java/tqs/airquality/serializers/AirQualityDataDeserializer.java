package tqs.airquality.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import tqs.airquality.models.AirQualityData;

import java.io.IOException;

public class AirQualityDataDeserializer extends StdDeserializer<AirQualityData> {

    public AirQualityDataDeserializer() {
        this(null);
    }

    public AirQualityDataDeserializer(Class<?> vc) {
        super(vc);
    }

    //TODO : Ver dps isto
    @Override
    public AirQualityData deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        double lat = (double) ((DoubleNode) node.get("lat")).numberValue();
        double lon = (double) ((DoubleNode) node.get("lon")).numberValue();
        String city_name = node.get("city_name").asText();
        String country_code = node.get("country_code").asText();
        String state_code = node.get("state_code").asText();

        String itemName = node.get("itemName").asText();
        int userId = (Integer) ((IntNode) node.get("createdBy")).numberValue();

        return new AirQualityData();
    }
}
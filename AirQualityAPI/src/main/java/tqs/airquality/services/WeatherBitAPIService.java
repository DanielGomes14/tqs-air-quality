package tqs.airquality.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.DoubleNode;
import org.apache.hc.core5.net.URIBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.models.Particles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class WeatherBitAPIService {
    @Value("${weatherbit_token}")
    private String weather_bit_token;

    @Autowired
    private BasicHttpClient basicHttpClient;

    public URIBuilder buildURI(String city) throws URISyntaxException {
        URIBuilder uriBuilder =  new URIBuilder("https://api.weatherbit.io/v2.0/current/airquality");
        if(city != null){
            uriBuilder.addParameter("city", city);
        }
        uriBuilder.addParameter("key","b425cc3001644d4bb6d9af3e4234d750");
        return uriBuilder;
    }

    public Optional<AirQualityData> fetchAPIDataByCityName(String cityName) throws URISyntaxException, IOException, ParseException {
        URIBuilder uriBuilder =  buildURI(cityName);
        String response = this.basicHttpClient.get(uriBuilder.build().toString());
        AirQualityData data = null;
        if(response != null){
            JSONObject obj = (JSONObject) new JSONParser().parse(response);
            data = new ObjectMapper().readValue(obj.toString(), AirQualityData.class);
        }
        return data == null? Optional.empty() : Optional.of(data);
    }


}

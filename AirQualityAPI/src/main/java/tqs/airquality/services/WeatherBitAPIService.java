package tqs.airquality.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.models.Cache;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@Service
public class WeatherBitAPIService {
    @Value("${weatherbit_token}")
    private String weatherbittoken;

    private  final Cache cache = new Cache() ;

    @Autowired
    private BasicHttpClient basicHttpClient;



    public String buildURI(String city, String countrycode) throws URISyntaxException {
        var uriBuilder =  new URIBuilder("https://api.weatherbit.io/v2.0/current/airquality");
        if(city != null){
            uriBuilder.addParameter("city", city.trim());
        }
        if(countrycode != null){
            uriBuilder.addParameter("country", countrycode.trim());
        }
        uriBuilder.addParameter("key","b425cc3001644d4bb6d9af3e4234d750");
        return uriBuilder.build().toString();
    }

    public Optional<AirQualityData> fetchAPIDataByCityNameAndCountry(String cityName, String countrycode) throws URISyntaxException, IOException, ParseException {
        var uriString =  buildURI(cityName,countrycode);
        String response = this.cache.get(uriString);
        if(response == null) {
            response = this.basicHttpClient.get(uriString);
            cache.put(uriString,response);
        }
        Optional<AirQualityData> data = Optional.empty();
        if(response != null){
            JSONObject obj = (JSONObject) new JSONParser().parse(response);
            data = Optional.of(new ObjectMapper().readValue(obj.toString(), AirQualityData.class));
        }
        return data;
    }


    public Cache getCacheObj(){
        return cache;
    }


}

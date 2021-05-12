package tqs.airquality.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.models.AirQualityDataForecast;
import tqs.airquality.models.Cache;
import tqs.airquality.models.Particles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherBitAPIService {
    @Value("${weatherbit_token}")
    private String weatherbittoken;

    private final Cache cache  = new Cache();
    private static final String CURRENT_BASE_URI = "https://api.weatherbit.io/v2.0/current/airquality";
    private static final String FORECAST_BASE_URI = "https://api.weatherbit.io/v2.0/forecast/airquality";

    @Autowired
    private BasicHttpClient basicHttpClient;



    public String buildURI(String city, String countrycode, String base_uri) throws URISyntaxException {
        var uriBuilder =  new URIBuilder(base_uri);
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
        var uriString =  buildURI(cityName,countrycode, CURRENT_BASE_URI);
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
    public Optional<AirQualityDataForecast> fetchForecast(String cityName, String countrycode) throws URISyntaxException, IOException, ParseException {
        var uriSttring = buildURI(cityName, countrycode, FORECAST_BASE_URI);
        String response = this.cache.get(uriSttring);
        if(response == null){
            response = this.basicHttpClient.get(uriSttring);
            cache.put(uriSttring,response);
        }

        Optional<AirQualityDataForecast> data = Optional.empty();
        try {

            JSONObject obj = (JSONObject) new JSONParser().parse(response);
            AirQualityDataForecast forecast = new ObjectMapper().readValue(obj.toString(),
                    AirQualityDataForecast.class);
            // the forecast is over 2 days, with 1 hour ahead, therefore, lets just
            // just skip it, until we got only 1 metric per day, as it is enough
            // therefore a total of 3 days counting with the current one
            List<Particles> particlesList = new ArrayList<>();
            for(int i = 0; i<= 2 ; i++) {
                particlesList.add(forecast.getData().get(0));
                forecast.setData(forecast.getData().stream().skip(24).collect(Collectors.toList()));
            }
            forecast.setData(particlesList);
            data = Optional.of(forecast);
        }
        catch (NullPointerException e){
            // In case the API returned some errors
            return data;
        }
        return data;
    }


    public Cache getCacheObj(){
        return cache;
    }


}

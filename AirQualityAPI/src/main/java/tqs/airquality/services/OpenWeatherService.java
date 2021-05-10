package tqs.airquality.services;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.net.URIBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tqs.airquality.models.AirQualityDataForecast;
import tqs.airquality.models.Cache;
import tqs.airquality.models.Particles;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpenWeatherService {

    @Value("${openweather_token}")
    private String openweathertoken;
    private static final String COMPONENTS = "components";
    private final Cache cache = new Cache();

    @Autowired
    private BasicHttpClient basicHttpClient;

    private String buildURI(Double lat, Double lon) throws URISyntaxException {
        var uriBuilder =  new URIBuilder("http://api.openweathermap.org/data/2.5/air_pollution/forecast");
        if(lat != null)
            uriBuilder.addParameter("lat", String.valueOf(lat));
        if(lon != null)
            uriBuilder.addParameter("lon", String.valueOf(lon));
        uriBuilder.addParameter("appid", "be6f711c16fab6ff16ceea0665cfc058");
        return uriBuilder.build().toString();
    }

    public Optional<AirQualityDataForecast> fetchForecast(double lat, double lon) throws URISyntaxException, IOException, ParseException {
        var uriSttring = buildURI(lat, lon);
        String response = this.cache.get(uriSttring);
            if(response == null){
            response = this.basicHttpClient.get(uriSttring);
            cache.put(uriSttring,response);
        }

        Optional<AirQualityDataForecast> data = Optional.empty();
        try {

            var dataForecast = new AirQualityDataForecast();
            JSONObject obj = (JSONObject) new JSONParser().parse(response);
            JsonNode node = new ObjectMapper().readTree(obj.toJSONString());
            dataForecast.setLat(node.get("coord").get("lat").doubleValue());
            dataForecast.setLon(node.get("coord").get("lon").doubleValue());
            List<Particles> particlesList = new ArrayList<>();
            for( JsonNode subnode : node.get("list")){
                var particles = new Particles();
                System.out.println(subnode.get(COMPONENTS));
                particles.setAqi(subnode.get("main").get("aqi").intValue());
                particles.setCo(subnode.get(COMPONENTS).get("co").doubleValue());
                particles.setNo2(subnode.get(COMPONENTS).get("no2").doubleValue());
                particles.setO3(subnode.get(COMPONENTS).get("o3").doubleValue());
                particles.setPm10(subnode.get(COMPONENTS).get("pm10").doubleValue());
                particles.setPm25(subnode.get(COMPONENTS).get("pm2_5").doubleValue());
                particlesList.add(particles);
            }
            // the forecast is over 5 days, with 1 hour ahead, therefore, lets just
            // just skip it, until we got only 1 metric per day, as it is enough
            // therefore a total of 6 days counting with the current one
            List<Particles> finalparticlesList = new ArrayList<>();
            for(int i = 0; i<= 5 ; i++) {
                finalparticlesList.add(particlesList.get(0));
                particlesList=particlesList.stream().skip(24).collect(Collectors.toList());
            }
            dataForecast.setData(finalparticlesList);
            data = Optional.of(dataForecast);
        }
        catch (NullPointerException e){
            // In case the API returned some errors
            return data;
        }
        return data;
    }
}

package tqs.airquality.utils;

import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;

public class URIHelper {
    public URIBuilder buildURIWeatherBit(String token, String city) throws URISyntaxException {
        URIBuilder uriBuilder =  new URIBuilder("https://api.weatherbit.io/v2.0/current/airquality");
        if(city != null){
            uriBuilder.addParameter("city", city);
        }
        uriBuilder.addParameter("key",token);
        return uriBuilder;
    }
}

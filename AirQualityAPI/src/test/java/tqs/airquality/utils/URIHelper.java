package tqs.airquality.utils;

import org.apache.hc.core5.net.URIBuilder;

import java.net.URISyntaxException;

public class URIHelper {
    public URIBuilder buildURIWeatherBit(String token, String city, String country) throws URISyntaxException {
        URIBuilder uriBuilder =  new URIBuilder("https://api.weatherbit.io/v2.0/current/airquality");
        if(city != null){
            uriBuilder.addParameter("city", city);
        }
        if(country != null){
            uriBuilder.addParameter("country", country);
        }
        uriBuilder.addParameter("key",token);
        return uriBuilder;
    }

    public URIBuilder buildURIOpenWeather(String token, Double lat, Double lon) throws URISyntaxException {
        URIBuilder uriBuilder =  new URIBuilder("http://api.openweathermap.org/data/2.5/air_pollution/forecast");
        if(lat != null){
            uriBuilder.addParameter("lat", String.valueOf(lat));
        }
        if(lon != null){
            uriBuilder.addParameter("lon", String.valueOf(lon));
        }
        uriBuilder.addParameter("appid",token);
        return uriBuilder;
    }
}

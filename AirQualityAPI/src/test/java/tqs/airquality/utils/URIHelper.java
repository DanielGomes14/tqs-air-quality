package tqs.airquality.utils;

import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class URIHelper {
    public URIBuilder buildURIWeatherBit(String URIBASE, String token, String city, String country) throws URISyntaxException {
        URIBuilder uriBuilder =  new URIBuilder(URIBASE);
        if(city != null){
            uriBuilder.addParameter("city", city);
        }
        if(country != null){
            uriBuilder.addParameter("country", country);
        }
        uriBuilder.addParameter("key",token);
        return uriBuilder;
    }

}

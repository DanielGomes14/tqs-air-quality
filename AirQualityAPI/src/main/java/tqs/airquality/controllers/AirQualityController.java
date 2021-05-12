package tqs.airquality.controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.models.AirQualityDataForecast;
import tqs.airquality.models.Cache;
import tqs.airquality.services.WeatherBitAPIService;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class AirQualityController {

    @Autowired
    private WeatherBitAPIService service;


    @GetMapping("/current_quality")
    public ResponseEntity <AirQualityData>getAirQuality(
            @RequestParam String cityname,
            @RequestParam String countrycode
    ) throws URISyntaxException, IOException, ParseException {
        /*
         Retrieves Air Quality Data given a cityname and its countrycode
         */
        if (cityname != null && countrycode != null ) {
            AirQualityData data = service.fetchAPIDataByCityNameAndCountry(cityname,countrycode).orElseThrow(()->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,"City Or Country Not Found")
            );
            return  ResponseEntity.ok().body(data);
        }
        throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "City and/or Parameters were not Provided");
    }

    @GetMapping("/forecast")
    public ResponseEntity<AirQualityDataForecast> getAirQualityForecast(
            @RequestParam String cityname,
            @RequestParam String countrycode
    ) throws URISyntaxException, IOException, ParseException {
        /*
         Retrieves today's Air Quality Data and the forecast for the next five days,
          given coordinates ( latitude and longitude)
         */
        if (cityname != null && countrycode != null ) {
            AirQualityDataForecast data = service.fetchForecast(cityname,countrycode).orElseThrow(() ->
             new ResponseStatusException(HttpStatus.NOT_FOUND,"Latitude and/or Longitude not valid."));
            return  ResponseEntity.ok().body(data);
        }
        throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "City and/or Parameters were not Provided");
    }

    @GetMapping("/cache-statistics")
    public ResponseEntity<Cache> getCacheStatistics(){
        /*
            Cache Statistics for Air Quality Data
         */
            return ResponseEntity.ok().body(service.getCacheObj());
    }
}

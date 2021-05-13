package tqs.airquality.controllers;


import io.swagger.annotations.ApiOperation;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class AirQualityController {

    @Autowired
    private WeatherBitAPIService service;
    private static final Logger logger = LogManager.getLogger(AirQualityController.class);

    @GetMapping("/current_quality")
    @ApiOperation("Get the current Air Quality Information")
    public ResponseEntity <AirQualityData>getAirQuality(
            @RequestParam String cityname,
            @RequestParam String countrycode
    ) throws URISyntaxException, IOException, ParseException {
        /*
         Retrieves Air Quality Data given a cityname and its countrycode
         */
        logger.info("New Request for current air quality");
        AirQualityData data = service.fetchAPIDataByCityNameAndCountry(cityname,countrycode).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,"City Or Country Not Found")
        );
        return  ResponseEntity.ok().body(data);

    }

    @GetMapping("/forecast")
    @ApiOperation(value = "Get current Time Air Quality Data and the forecast for the next two days" +
            " at this time. ")
    public ResponseEntity<AirQualityDataForecast> getAirQualityForecast(
            @RequestParam String cityname,
            @RequestParam String countrycode
    ) throws URISyntaxException, IOException, ParseException {
        /*
         Retrieves today's Air Quality Data and the forecast for the next two days,
          given coordinates ( latitude and longitude)
         */
        logger.info("New Request for forecast air quality");
        AirQualityDataForecast data = service.fetchForecast(cityname,countrycode).orElseThrow(() ->
         new ResponseStatusException(HttpStatus.NOT_FOUND,"Latitude and/or Longitude not valid."));
        logger.info("No error in Request");
        return  ResponseEntity.ok().body(data);

    }

    @GetMapping("/cache-statistics")
    @ApiOperation("Get Current Cache Statistics")
    public ResponseEntity<Cache> getCacheStatistics(){
        /*
            Cache Statistics for Air Quality Data
         */
        logger.info("New Request for Cache Statistics");
        return ResponseEntity.ok().body(service.getCacheObj());
    }
}

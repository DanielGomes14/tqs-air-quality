package tqs.airquality.controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.models.Cache;
import tqs.airquality.services.WeatherBitAPIService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class AirQualityController {

    @Autowired
    private WeatherBitAPIService service;

    @GetMapping("/current_quality")
    public ResponseEntity <Object>getAirQuality(
            @RequestParam String cityname,
            @RequestParam String countrycode
    ) throws URISyntaxException, IOException, ParseException {
        if (cityname != null && countrycode != null ) {
            Optional<AirQualityData> data = service.fetchAPIDataByCityNameAndCountry(cityname,countrycode);
            if(data.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND,"City Or Country Not Found");
            return  ResponseEntity.ok().body(data);
        }
        throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "City and/or Parameters were not Provided");
    }

    @GetMapping("/cache-statistics")
    public ResponseEntity<Cache> getCacheStatistics(){
            return ResponseEntity.ok().body(this.service.getCacheObj());
    }
}

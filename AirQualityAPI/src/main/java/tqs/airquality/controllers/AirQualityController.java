package tqs.airquality.controllers;


import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.services.WeatherBitAPIService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/")
public class AirQualityController {

    @Autowired
    private WeatherBitAPIService service;

    @CrossOrigin(origins = "*")
    @GetMapping("/current_quality")
    public ResponseEntity<AirQualityData> getAirQuality(
            @RequestParam String cityname
    ) throws URISyntaxException, IOException, ParseException {
        if (cityname != null){
            Optional<AirQualityData> data = service.fetchAPIDataByCityName(cityname);
            if(data.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return  ResponseEntity.of(data);

        }
        return null;
    }
}

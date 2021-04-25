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

    @GetMapping("/current_quality")
    public ResponseEntity<?> getAirQuality(
            @RequestParam String city_name
    ) throws URISyntaxException, IOException, ParseException {
        if (city_name != null){
            Optional<AirQualityData> data = service.fetchAPIDataByCityName(city_name);
            if (data.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("City Name Invalid");

            return  ResponseEntity.status(HttpStatus.OK).body(data);

        }
        return null;
    }
}

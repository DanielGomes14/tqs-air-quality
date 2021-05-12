package tqs.airquality.controllers;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tqs.airquality.models.City;
import tqs.airquality.services.CityService;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class CityController {
    @Autowired
    private CityService cityService;

    private static final Logger logger = LogManager.getLogger(CityController.class);


    @GetMapping("/cities")
    public ResponseEntity<List<City>> findAllCities(){
        /*
            Returns a set of 50 cities
         */
        logger.info("Returning all cities available");
        return ResponseEntity.ok().body(cityService.findAllCities());
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<City> findCityById(
            @PathVariable(value = "id") long id
    ){
        /*
            Returns a city given it's id
         */
        logger.info("New Request for city");
        City city = cityService.findCityById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "City with id ::" + id + "not found"
        ));
        logger.info("Returning city");
        return ResponseEntity.ok().body(city);
    }
}

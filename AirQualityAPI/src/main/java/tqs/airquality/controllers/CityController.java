package tqs.airquality.controllers;


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

    @GetMapping("/cities")
    public ResponseEntity<List<City>> findAllCities(){
        return ResponseEntity.ok().body(cityService.findAllCities());
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<City> findCityById(
            @PathVariable(value = "id") long id
    ){
        City city = cityService.findCityById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "City with id ::" + id + "not found"
        ));
        return ResponseEntity.ok().body(city);
    }
}

package tqs.airquality.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tqs.airquality.models.City;
import tqs.airquality.repositories.CityRepository;
import tqs.airquality.utils.CSVHelper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository cityRepository;
    private final  CSVHelper csvHelper = new CSVHelper();

    public List<City> checkCitiesList(){
        List<City> listcities = cityRepository.findAll();
        if(listcities.isEmpty()){
            listcities= this.csvHelper.readCSVDataToArray("cities_20000.csv").stream().limit(50).collect(Collectors.toList());
            cityRepository.saveAll(listcities);
            return listcities;
        }
        return listcities;
    }

    public List<City> findAllCities(){
        return checkCitiesList();
    }
    public Optional<City> findCityById(long cityId){
        checkCitiesList();
        // the process of searching through the database is quicker than iterating a list...
        return cityRepository.findById(cityId);
    }


}

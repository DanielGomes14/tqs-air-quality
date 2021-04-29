package tqs.airquality.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.airquality.models.City;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CityRepositoryTest    {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void whenFindCityByExisting_Id_thenReturnCity(){
        City city = new City(1L,"Aveiro", "AV", "PT", "Portugal",40.64427, -8.64554);
        entityManager.persistAndFlush(city);
        City cityDbObj = cityRepository.findById(city.getCityid()).orElse(null);
        assertThat(cityDbObj).isNotNull();
    }
    @Test
    void whenInvalidId_thenReturnNull(){
        City cityDbObj =  cityRepository.findById(-99L).orElse(null);
        assertThat(cityDbObj).isNull();
    }

    @Test
    void givenSetOfCities_whenFindAll_thenReturnAllCities(){
        City city = new City(1L,"Aveiro", "AV", "PT", "Portugal",40.64427, -8.64554);
        City city2 = new City(2L,"Mangualde", "VS", "PT", "Portugal",40.6046, 7.7639);
        City city3 = new City(3L,"Viseu", "VS", "PT", "Portugal",40.7276, 7.9157);
        Arrays.asList(city,city2,city3).forEach(
                (city_for -> {
                    entityManager.persist(city_for);
                } )
        );
        entityManager.flush();
        List<City> allCities = cityRepository.findAll();
        assertThat(allCities).hasSize(3).extracting(
                City::getCityname).containsOnly(city.getCityname(),
                city2.getCityname(), city3.getCityname());
    }
}
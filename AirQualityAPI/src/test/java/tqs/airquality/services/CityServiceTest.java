package tqs.airquality.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import tqs.airquality.models.City;
import tqs.airquality.repositories.CityRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock(lenient = true)
    private CityRepository cityRepository;

    @InjectMocks
    private CityService service;

    @BeforeEach
    void setUp(){
        City city = new City(1L,"Aveiro", "AV", "PT", "Portugal",40.64427, -8.64554);
        City city2 = new City(2L,"Mangualde", "VS", "PT", "Portugal",40.6046, 7.7639);
        List<City> allcities = Arrays.asList(city,city2);
        when(cityRepository.findById(city.getCityid())).thenReturn(Optional.of(city));
        when(cityRepository.findAll()).thenReturn(allcities);
    }

    @Test
    void whenValidId_thenCityShouldBeFound(){
        long cityId = 1L;
        City city = service.findCityById(cityId).orElse(null);
        assertThat(city).isNotNull();
        assertThat(city.getCityid()).isEqualTo(cityId);
        verifyFindByIdIsCalledOnce();
    }
    @Test
    void whenInvalidId_then_CityShouldNotbeFound(){
        Optional<City> notfound = service.findCityById(-99L);
        verifyFindByIdIsCalledOnce();
        assertThat(notfound).isEmpty();
    }
    @Test
     void given2Cities_whenGetAll_thenReturn2Records() {
        City city = new City(1L,"Aveiro", "AV", "PT", "Portugal",40.64427, -8.64554);
        City city2 = new City(2L,"Mangualde", "VS", "PT", "Portugal",40.6046, 7.7639);
        List<City> allCities = service.findAllCities();
        verifyFindAllCitiesisCalledOnce();
        assertThat(allCities)
                .hasSize(2)
                .extracting(City::getCityname)
                .contains(city.getCityname(),city2.getCityname());
    }
    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(cityRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
    }
    private void verifyFindAllCitiesisCalledOnce(){
        Mockito.verify(cityRepository, VerificationModeFactory.times(1)).findAll();
    }
}

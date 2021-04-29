package tqs.airquality.controllers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.airquality.models.City;
import tqs.airquality.services.CityService;

import java.util.Arrays;
import java.util.Optional;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CityController.class)
class CityControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private CityService service;
    private static final String ALLCITIES_ENDPOINT = "/api/v1/cities";
    private static final String CITY_ENDPOINT= "/api/v1/cities/1";
    private static final String INVALID_CITY_ENDPOINT= "/api/v1/cities/9999";

    @Test
    void whenGetCitiesById_thenReturnCity() throws Exception {
        City city = new City(1L,"Aveiro", "AV", "PT", "Portugal",40.64427, -8.64554);
        when(service.findCityById(anyLong())).thenReturn(Optional.of(city));
        mvc.perform(get(CITY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.cityname", is("Aveiro")));
    }

    @Test
    void whenInvalidId_thenReturnNull() throws Exception{
        when(service.findCityById(9999)).thenReturn(Optional.empty());
        mvc.perform(get(INVALID_CITY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(service, times(1)).findCityById(9999);
    }

    @Test
    void  whenGetCities_thenReturnCities() throws Exception {
        City city = new City(1L,"Aveiro", "AV", "PT", "Portugal",40.64427, -8.64554);
        City city2 = new City(2L,"Mangualde", "VS", "PT", "Portugal",40.6046, 7.7639);
        when(service.findAllCities()).thenReturn(Arrays.asList(city,city2));
        mvc.perform(get(ALLCITIES_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].cityname", is(city.getCityname())))
                .andExpect(jsonPath("$[1].cityname", is(city2.getCityname())));
    }

}

package tqs.airquality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.airquality.models.City;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class CityControllerIT {


    private static final String CITY_DETAILS_ENDPOINT = "/api/v1/cities/2509954";
    private static final String INVALID_CITY_DETAILS_ENDPOINT = "/api/v1/cities/0000";
    private static final String ALL_CITIES_DETAILS_ENDPOINT = "/api/v1/cities";

    @Autowired
    private MockMvc mvc;

    @Test
    void whenGetCity_returnCity() throws Exception {
        String json_res = getJsonResponse();
        City city_obj = new ObjectMapper().readValue(json_res,City.class);
        mvc.perform(get(CITY_DETAILS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lat", is(city_obj.getLat())))
                .andExpect(jsonPath("$.lon", is(city_obj.getLon())))
                .andExpect(jsonPath("$.cityname", is(city_obj.getCityname())));

    }
    @Test
    void whenBadCityId_returnNotFound() throws Exception {
        mvc.perform(get(INVALID_CITY_DETAILS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void  whenGetCities_returnCities() throws Exception {
        mvc.perform(get(ALL_CITIES_DETAILS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }


    String getJsonResponse(){
        return  "{\"cityid\":2509954,\"cityname\":\"Valencia\",\"statecode\":\"60\",\"countrycode\":\"ES\"," +
                "\"countyfull\":\"Spain\",\"lat\":39.46975,\"lon\":-0.37739}";
    }
}

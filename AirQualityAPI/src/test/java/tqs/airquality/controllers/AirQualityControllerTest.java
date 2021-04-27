package tqs.airquality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.services.WeatherBitAPIService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AirQualityController.class)
class AirQualityControllerTest {

    private static final String CITY_NAME = "Mangualde";
    private static final String INVALID_CITY_NAME = "UmaCidadeInvalida";
    private static final String CITY_ENDPOINT= String.format("/api/v1/current_quality?cityname=%s",CITY_NAME);
    private static final String INVALID_CITY_ENDPOINT= String.format("/api/v1/current_quality?cityname=%s",INVALID_CITY_NAME);

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherBitAPIService service;

    @Test
    void whenGetQualityByCity_thenReturnQuality() throws Exception {
        String json_res = "{\"lat\":40.60425,\"lon\":-7.76115,\"timezone\":\"Europe/Lisbon\"," +
                "\"city_name\":\"Mangualde\",\"country_code\":\"PT\",\"state_code\":\"22\"," +
                "\"data\":[{\"aqi\":62,\"o3\":129.998,\"so2\":0.584871,\"no2\":0.532164,\"co\":305.831," +
                "\"pm25\":0.0953826,\"pm10\":0.433322,\"pollen_level_tree\":1.0,\"pollen_level_grass\":1.0," +
                "\"pollen_level_weed\":1.0,\"mold_level\":1,\"predominant_pollen_type\":\"Molds\"}]}";

        AirQualityData json_obj = new ObjectMapper().readValue(json_res, AirQualityData.class);
        when(service.fetchAPIDataByCityName(CITY_NAME)).thenReturn(
                Optional.of(json_obj)
        );
        mvc.perform(get(CITY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lat", is(json_obj.getLat())))
                .andExpect(jsonPath("$.lon", is(json_obj.getLon())))
                .andExpect(jsonPath("$.city_name", is(json_obj.getCity_name())))
                .andExpect(jsonPath("$.country_code", is(json_obj.getCountry_code())))
                .andExpect(jsonPath("$.timezone", is(json_obj.getTimezone())))
                .andExpect(jsonPath("$.data", hasSize(equalTo(1))));

        verify(service, times(1)).fetchAPIDataByCityName(CITY_NAME);
    }
    @Test
    void when_BadCityName_thenReturnNotFound() throws Exception {
        when(service.fetchAPIDataByCityName(INVALID_CITY_NAME)).thenReturn(
            Optional.empty()
        );
        mvc.perform(get(INVALID_CITY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service, times(1)).fetchAPIDataByCityName(INVALID_CITY_NAME);
    }

}
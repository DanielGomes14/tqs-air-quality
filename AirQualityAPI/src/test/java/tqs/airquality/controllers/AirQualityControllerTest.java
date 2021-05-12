package tqs.airquality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.models.AirQualityDataForecast;
import tqs.airquality.services.WeatherBitAPIService;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;


import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AirQualityController.class)
class AirQualityControllerTest {

    private static final String CITY_NAME = "Mangualde";
    private static final String INVALID_CITY_NAME = "UmaCidadeInvalida";
    private static final String COUNTRY_CODE ="PT";
    private static final String INVALID_COUNTRY_CODE ="FFFFF";
    private static final String BASE_FORECAST_URI ="/api/v1/forecast";
    private static final String BASE_QUALITY_URI = "/api/v1/current_quality";
    private static final String CITY_ENDPOINT= String.format(
            "%s?cityname=%s&countrycode=%s", BASE_QUALITY_URI, CITY_NAME,COUNTRY_CODE);
    private static final String INVALID_COUNTRY_ENDPOINT= String.format(
            "%s?cityname=%s&countrycode=%s",BASE_QUALITY_URI,CITY_NAME,INVALID_COUNTRY_CODE);
    private static final String INVALID_CITY_AND_COUNTRY_ENDPOINT= String.format(
            "%s?cityname=%s&countrycode=%s",BASE_QUALITY_URI,INVALID_CITY_NAME,INVALID_COUNTRY_CODE);
    private static final String INVALID_CITY_ENDPOINT= String.format(
            "%s?cityname=%s&countrycode=%s",BASE_QUALITY_URI,INVALID_CITY_NAME,COUNTRY_CODE);
    private static final String FORECAST_ENDPOINT = String.format(
            "%s?cityname=%s&countrycode=%s", BASE_FORECAST_URI, CITY_NAME,COUNTRY_CODE
    );
    private static final String INVALID_CITY_FORECAST_ENDPOINT = String.format(
            "%s?cityname=%s&countrycode=%s", BASE_FORECAST_URI, INVALID_CITY_NAME,COUNTRY_CODE
    );
    private static final String INVALID_COUNTRY_FORECAST_ENDPOINT = String.format(
            "%s?cityname=%s&countrycode=%s", BASE_FORECAST_URI, CITY_NAME,INVALID_COUNTRY_CODE
    );
    private static final String INVALID_CITY_COUNTRY_FORECAST_ENDPOINT = String.format(
            "%s?cityname=%s&countrycode=%s", BASE_FORECAST_URI, INVALID_CITY_NAME,INVALID_COUNTRY_CODE
    );


    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherBitAPIService service;


    @Test
    void whenGetQualityByCityAndCountry_thenReturnQuality() throws Exception {
        String json_res = createJsonResponse();
        AirQualityData json_obj = new ObjectMapper().readValue(json_res, AirQualityData.class);
        when(service.fetchAPIDataByCityNameAndCountry(CITY_NAME,COUNTRY_CODE)).thenReturn(
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

        verify(service, times(1)).fetchAPIDataByCityNameAndCountry(CITY_NAME,COUNTRY_CODE);
    }
    @Test
    void when_BadCityName_thenReturnNotFound() throws Exception {
        when(service.fetchAPIDataByCityNameAndCountry(INVALID_CITY_NAME,COUNTRY_CODE)).thenReturn(
            Optional.empty()
        );
        mvc.perform(get(INVALID_CITY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service, times(1)).fetchAPIDataByCityNameAndCountry(INVALID_CITY_NAME,COUNTRY_CODE);
    }

    @Test
    void whenBadCountry_thenReturnNotFound() throws Exception {
        when(service.fetchAPIDataByCityNameAndCountry(CITY_NAME,INVALID_COUNTRY_CODE)).thenReturn(
                Optional.empty()
        );
        mvc.perform(get(INVALID_COUNTRY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service, times(1)).fetchAPIDataByCityNameAndCountry(CITY_NAME,INVALID_COUNTRY_CODE);
    }

    @Test
    void whenBadCountryAndBadCity_thenReturnNotFound() throws Exception {
        when(service.fetchAPIDataByCityNameAndCountry(INVALID_CITY_NAME,INVALID_COUNTRY_CODE)).thenReturn(
                Optional.empty()
        );
        mvc.perform(get(INVALID_CITY_AND_COUNTRY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service, times(1)).fetchAPIDataByCityNameAndCountry(INVALID_CITY_NAME,INVALID_COUNTRY_CODE);
    }

    @Test
    void whenForecast_thenReturnForecast() throws Exception {
        String json_res = createForecastJsonResponse();
        AirQualityDataForecast json_obj = new ObjectMapper().readValue(json_res, AirQualityDataForecast.class);
        when(service.fetchForecast(CITY_NAME,COUNTRY_CODE)).thenReturn(
                Optional.of(json_obj)
        );
        mvc.perform(get(FORECAST_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lat", is(json_obj.getLat())))
                .andExpect(jsonPath("$.lon", is(json_obj.getLon())))
                .andExpect(jsonPath("$.data", hasSize(equalTo(3))));

        verify(service, times(1)).fetchForecast(CITY_NAME, COUNTRY_CODE);
    }

    @Test
    void whenForecastCityNameWrong_thenReturnNotFound() throws  Exception {
        when(service.fetchForecast(INVALID_CITY_NAME,COUNTRY_CODE)).thenReturn(
                Optional.empty()
        );
        mvc.perform(get(INVALID_CITY_FORECAST_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service, times(1)).fetchForecast(INVALID_CITY_NAME,COUNTRY_CODE);
    }

    @Test
    void whenForecastCountryWrong_thenReturnNotFound() throws  Exception {
        when(service.fetchForecast(CITY_NAME, INVALID_COUNTRY_CODE)).thenReturn(
                Optional.empty()
        );
        mvc.perform(get(INVALID_COUNTRY_FORECAST_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service, times(1)).fetchForecast(CITY_NAME,INVALID_COUNTRY_CODE);
    }
    @Test
    void whenForecastCityNameCounryWrong_thenReturnNotFound() throws  Exception {
        when(service.fetchForecast(INVALID_CITY_NAME, INVALID_COUNTRY_CODE)).thenReturn(
                Optional.empty()
        );
        mvc.perform(get(INVALID_CITY_COUNTRY_FORECAST_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service, times(1)).fetchForecast(INVALID_CITY_NAME,INVALID_COUNTRY_CODE);
    }


    String createJsonResponse(){
        return "{\"lat\":40.60425,\"lon\":-7.76115,\"timezone\":\"Europe/Lisbon\"," +
                "\"city_name\":\"Mangualde\",\"country_code\":\"PT\",\"state_code\":\"22\"," +
                "\"data\":[{\"aqi\":62,\"o3\":129.998,\"so2\":0.584871,\"no2\":0.532164,\"co\":305.831," +
                "\"pm25\":0.0953826,\"pm10\":0.433322,\"pollen_level_tree\":1.0,\"pollen_level_grass\":1.0," +
                "\"pollen_level_weed\":1.0,\"mold_level\":1,\"predominant_pollen_type\":\"Molds\"}]}";
    }
    String createForecastJsonResponse(){
        return "{\"lat\":40.60425,\"lon\":-7.76115,\"timezone\":\"Europe/Lisbon\"," +
                "\"city_name\":\"Mangualde\",\"country_code\":\"PT\",\"state_code\":\"22\"," +
                "\"data\":[{\"aqi\":49,\"o3\":105.143,\"so2\":0.709668,\"no2\":0.362808,\"co\":299.156," +
                "\"pm25\":4.55646,\"pm10\":20.006},{\"aqi\":46,\"o3\":98.8841,\"so2\":0.484288,\"no2\":0.388914," +
                "\"co\":294.149,\"pm25\":4.26406,\"pm10\":18.0403},{\"aqi\":53,\"o3\":114.977,\"so2\":0.590459,\"no2\":0.499364," +
                "\"co\":284.135,\"pm25\":0.699316,\"pm10\":1.72896}]}";
    }


}

package tqs.airquality.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.models.AirQualityDataForecast;
import tqs.airquality.services.OpenWeatherService;
import tqs.airquality.services.WeatherBitAPIService;


import java.io.IOException;
import java.net.URISyntaxException;
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
    private static final Double VALID_LAT = 40.60425;
    private static final Double VALID_LON = -7.76115;
    private static final String BASE_FORECAST_URI ="/api/v1/forecast";
    private static final String BASE_QUALITY_URI = "/api/v1/current_quality";
    private static final String CACHE_ENDPOINT = "/api/v1/cache-statistics";
    private static final String CITY_ENDPOINT= String.format(
            "%s?cityname=%s&countrycode=%s", BASE_QUALITY_URI, CITY_NAME,COUNTRY_CODE);
    private static final String INVALID_COUNTRY_ENDPOINT= String.format(
            "%s?cityname=%s&countrycode=%s",BASE_QUALITY_URI,CITY_NAME,INVALID_COUNTRY_CODE);
    private static final String INVALID_CITY_AND_COUNTRY_ENDPOINT= String.format(
            "%s?cityname=%s&countrycode=%s",BASE_QUALITY_URI,INVALID_CITY_NAME,INVALID_COUNTRY_CODE);
    private static final String INVALID_CITY_ENDPOINT= String.format(
            "%s?cityname=%s&countrycode=%s",BASE_QUALITY_URI,INVALID_CITY_NAME,COUNTRY_CODE);
    private static final String FORECAST_ENDPOINT = String.format(
            "%s?lat=%f&lon=%f", BASE_FORECAST_URI, VALID_LAT,VALID_LON
    );
    private static final String INVALID_LAT_ENDPOINT = String.format(
            "%s?lat=%f&lon=%f", BASE_FORECAST_URI, -200.0,VALID_LON
    );
    private static final String INVALID_LON_ENDPOINT = String.format(
            "%s?lat=%f&lon=%f", BASE_FORECAST_URI, VALID_LAT,-200.0
    );
    private static final String INVALID_LON_LAT_ENDPOINT = String.format(
            "%s?lat=%f&lon=%f", BASE_FORECAST_URI, -200.0,-200.0
    );


    @Autowired
    private MockMvc mvc;

    @MockBean
    private WeatherBitAPIService service;

    @MockBean
    private OpenWeatherService service2;

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
        when(service2.fetchForecast(VALID_LAT,VALID_LON)).thenReturn(
                Optional.of(json_obj)
        );
        mvc.perform(get(FORECAST_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lat", is(json_obj.getLat())))
                .andExpect(jsonPath("$.lon", is(json_obj.getLon())))
                .andExpect(jsonPath("$.data", hasSize(equalTo(5))));

        verify(service2, times(1)).fetchForecast(VALID_LAT, VALID_LON);
    }

    @Test
    void whenForecastLatWrong_thenReturnNotFound() throws  Exception {
        when(service2.fetchForecast(-200.0,VALID_LON)).thenReturn(
                Optional.empty()
        );
        mvc.perform(get(INVALID_LAT_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service2, times(1)).fetchForecast(-200.0,VALID_LON);
    }

    @Test
    void whenForecastLonWrong_thenReturnNotFound() throws  Exception {
        when(service2.fetchForecast(VALID_LAT, -200.0)).thenReturn(
                Optional.empty()
        );
        mvc.perform(get(INVALID_LON_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service2, times(1)).fetchForecast(VALID_LAT,-200.0);
    }
    @Test
    void whenForecastLatLonWrong_thenReturnNotFound() throws  Exception {
        when(service2.fetchForecast(-200.0, -200.0)).thenReturn(
                Optional.empty()
        );
        mvc.perform(get(INVALID_LON_LAT_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(service2, times(1)).fetchForecast(-200.0,-200.0);
    }


    String createJsonResponse(){
        return "{\"lat\":40.60425,\"lon\":-7.76115,\"timezone\":\"Europe/Lisbon\"," +
                "\"city_name\":\"Mangualde\",\"country_code\":\"PT\",\"state_code\":\"22\"," +
                "\"data\":[{\"aqi\":62,\"o3\":129.998,\"so2\":0.584871,\"no2\":0.532164,\"co\":305.831," +
                "\"pm25\":0.0953826,\"pm10\":0.433322,\"pollen_level_tree\":1.0,\"pollen_level_grass\":1.0," +
                "\"pollen_level_weed\":1.0,\"mold_level\":1,\"predominant_pollen_type\":\"Molds\"}]}";
    }
    String createForecastJsonResponse(){
        return "{\"lat\":40.6043,\"lon\":-7.7612,\"data\":[{\"aqi\":1,\"o3\":16.27," +
                "\"so2\":0.0,\"no2\":6.51,\"co\":181.91,\"pm25\":16.27,\"pm10\":16.27}," +
                "{\"aqi\":1,\"o3\":56.51,\"so2\":0.0,\"no2\":4.07,\"co\":193.6,\"pm25\":56.51,\"pm10\":56.51}," +
                "{\"aqi\":1,\"o3\":59.37,\"so2\":0.0,\"no2\":3.81,\"co\":195.27,\"pm25\":59.37,\"pm10\":59.37}," +
                "{\"aqi\":1,\"o3\":75.82,\"so2\":0.0,\"no2\":1.46,\"co\":186.92,\"pm25\":75.82,\"pm10\":75.82}," +
                "{\"aqi\":1,\"o3\":29.33,\"so2\":0.0,\"no2\":4.63,\"co\":168.56,\"pm25\":29.33,\"pm10\":29.33}]}";
    }


}

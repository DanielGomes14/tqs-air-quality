package tqs.airquality.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tqs.airquality.models.AirQualityData;
import tqs.airquality.models.AirQualityDataForecast;

@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class AirQualityControllerIT {

    private static final String CITY_NAME = "Mangualde";
    private static final String INVALID_CITY_NAME = "UmaCidadeInvalida";
    private static final String COUNTRY_CODE ="PT";
    private static final String INVALID_COUNTRY_CODE ="FFFFF";
    private static final String BASE_QUALITY_URI = "/api/v1/current_quality";
    private static final String BASE_FORECAST_URI ="/api/v1/forecast";
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
    @Test
    void whenGetQualityByCityNameAndCountry_thenReturnQuality() throws Exception {
        String json_res = createJsonResponse();
        AirQualityData json_obj = new ObjectMapper().readValue(json_res, AirQualityData.class);
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
    }

    @Test
    void when_BadCityName_thenReturnNotFound() throws Exception {
        mvc.perform(get(INVALID_CITY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenBadCountry_thenReturnNotFound() throws Exception {
        mvc.perform(get(INVALID_COUNTRY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void whenBadCountryAndBadCity_thenReturnNotFound() throws Exception{
        mvc.perform(get(INVALID_CITY_AND_COUNTRY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetForecastByCityNameAndCountry_thenReturnQuality() throws Exception {
        String json_res = createJsonForecastResponse();
        AirQualityDataForecast json_obj = new ObjectMapper().readValue(json_res, AirQualityDataForecast.class);
        mvc.perform(get(FORECAST_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lat", is(json_obj.getLat())))
                .andExpect(jsonPath("$.lon", is(json_obj.getLon())))
                .andExpect(jsonPath("$.city_name", is(json_obj.getCity_name())))
                .andExpect(jsonPath("$.country_code", is(json_obj.getCountry_code())))
                .andExpect(jsonPath("$.timezone", is(json_obj.getTimezone())))
                .andExpect(jsonPath("$.data", hasSize(equalTo(3))));
    }

    @Test
    void whenGetForecastByInvalidCity_thenReturnNotFound() throws Exception {
        mvc.perform(get(INVALID_CITY_FORECAST_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void whenGetForecastByInvalidCountry_thenReturnNotFound() throws Exception {
        mvc.perform(get(INVALID_COUNTRY_FORECAST_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void whenGetForecastByInvalidCityAndCountry_thenReturnNotFound() throws Exception {
        mvc.perform(get(INVALID_CITY_AND_COUNTRY_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void testCacheStatistics() throws Exception {
        mvc.perform(get(CACHE_ENDPOINT).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void testCacheResults() throws Exception {
        //perfom two Requests, in order to check Cache
        for(int i = 0; i<=1; i++) mvc.perform(get(CITY_ENDPOINT).contentType(MediaType.APPLICATION_JSON));

        mvc.perform(get(CACHE_ENDPOINT).contentType(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberRequests",greaterThanOrEqualTo(2)))
                .andExpect(jsonPath("$.numberMisses",greaterThanOrEqualTo(1)))
                .andExpect(jsonPath("$.numberHits",greaterThanOrEqualTo(1)));

    }

    /*
        Test whenever some/or all parameters were not passed within the
        Endpoint
     */
    @Test
    void whenNotParemetersGiven_thenReturnBadRequest() throws Exception {
        mvc.perform(get(BASE_QUALITY_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void whenNoParametersForForecastGiven_thenReturnBadRequest() throws Exception {
        mvc.perform(get(BASE_FORECAST_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void whenNoCityParameterGiven_thenReturnBadRequest() throws Exception {
       String endpoint =  BASE_QUALITY_URI + "?cityname=" + COUNTRY_CODE;
        mvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void NoCityParameterGivenForForecast_thenReturnBadRequest() throws Exception {
        String endpoint =  BASE_FORECAST_URI + "?cityname=" + COUNTRY_CODE;
        mvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenNoCountryParameterGiven_thenReturnBadRequest() throws  Exception {
        String endpoint =  BASE_QUALITY_URI + "?countrycode=" + CITY_NAME;
        mvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenNoCountryParameterGivenForForecast_thenReturnBadRequest() throws  Exception {
        String endpoint =  BASE_FORECAST_URI + "?countrycode=" + CITY_NAME;
        mvc.perform(get(endpoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    String createJsonResponse(){
        return "{\"lat\":40.60425,\"lon\":-7.76115,\"timezone\":\"Europe/Lisbon\"," +
                "\"city_name\":\"Mangualde\",\"country_code\":\"PT\",\"state_code\":\"22\"," +
                "\"data\":[{\"aqi\":62,\"o3\":129.998,\"so2\":0.584871,\"no2\":0.532164,\"co\":305.831," +
                "\"pm25\":0.0953826,\"pm10\":0.433322,\"pollen_level_tree\":1.0,\"pollen_level_grass\":1.0," +
                "\"pollen_level_weed\":1.0,\"mold_level\":1,\"predominant_pollen_type\":\"Molds\"}]}";
    }
    String createJsonForecastResponse(){
        return  "{\"lat\":40.60425,\"lon\":-7.76115,\"timezone\":\"Europe/Lisbon\"," +
                "\"city_name\":\"Mangualde\",\"country_code\":\"PT\",\"state_code\":\"22\"," +
                "\"data\":[{\"aqi\":49,\"o3\":105.143,\"so2\":0.709668,\"no2\":0.362808,\"co\":299.156,\"pm25\":4.55646,\"pm10\":20.006}," +
                "{\"aqi\":46,\"o3\":98.8841,\"so2\":0.484288,\"no2\":0.388914,\"co\":294.149,\"pm25\":4.26406,\"pm10\":18.0403}," +
                "{\"aqi\":53,\"o3\":114.977,\"so2\":0.590459,\"no2\":0.499364,\"co\":284.135,\"pm25\":0.699316,\"pm10\":1.72896}]}";
    }

}

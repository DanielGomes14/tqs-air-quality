package tqs.airquality.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.CoreMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;


import tqs.airquality.models.AirQualityData;
import tqs.airquality.utils.URIHelper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class WeatherBitAPIServiceTest {

    @Mock(lenient = true)
    private BasicHttpClient httpClient;

    @InjectMocks
    private WeatherBitAPIService service;

    private static  final String API_TOKEN = "b425cc3001644d4bb6d9af3e4234d750";
    private static  final String CITY = "Mangualde";
    private static  final String INVALID_CITY = "UmaCidadeInvalida";
    private static final String COUNTRY ="PT";
    private static final String INVALID_COUNTRY ="FFFFF";

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        String json_res = createJsonResponse();
        String final_url =  new URIHelper().buildURIWeatherBit(API_TOKEN,CITY,COUNTRY).build().toString();
        String wrong_url = new URIHelper().buildURIWeatherBit(API_TOKEN,CITY,INVALID_COUNTRY).build().toString();
        String wrong_url_2 = new URIHelper().buildURIWeatherBit(API_TOKEN,INVALID_CITY,COUNTRY).build().toString();
        String wrong_url_3= new URIHelper().buildURIWeatherBit(API_TOKEN,INVALID_CITY,INVALID_COUNTRY).build().toString();
        when(httpClient.get(final_url)).thenReturn(json_res);
        when(httpClient.get(wrong_url)).thenReturn(null);
        when(httpClient.get(wrong_url_2)).thenReturn(null);
        when(httpClient.get(wrong_url_3)).thenReturn(null);
    }

    @Test
    void whenGetQualityByCityNameAndCountry_thenReturnCorrectQualityData() throws IOException, URISyntaxException, ParseException {
        String json_res = createJsonResponse();
        AirQualityData json_obj = new ObjectMapper().readValue(json_res, AirQualityData.class);
        AirQualityData res_service = service.fetchAPIDataByCityNameAndCountry(CITY, COUNTRY).orElse(null);
        assertThat(res_service).isNotNull();
        assertThat(res_service.getCity_name(), is(json_obj.getCity_name()));
        assertThat(res_service,is(json_obj));
    }
    @Test
    void whenGetQualityByBadCityNameAndCountry_thenReturnIncorrectData() throws IOException, URISyntaxException, ParseException {

        Optional<AirQualityData> res_service = service.fetchAPIDataByCityNameAndCountry(INVALID_CITY,COUNTRY);
        assertThat(res_service).isEmpty();
    }

    @Test
    void whenGetQualityByCityNameAndBadCountry_thenReturnIncorrectData() throws IOException, URISyntaxException, ParseException {
        Optional<AirQualityData> res_service = service.fetchAPIDataByCityNameAndCountry(CITY,INVALID_COUNTRY);
        assertThat(res_service).isEmpty();
    }
    @Test
    void whenGetQualityByBadCityNameAndBadCountry_thenReturnIncorrectData() throws IOException, URISyntaxException, ParseException {
        Optional<AirQualityData> res_service = service.fetchAPIDataByCityNameAndCountry(INVALID_CITY,INVALID_COUNTRY);
        assertThat(res_service).isEmpty();
    }

    String createJsonResponse(){
        return "{\"lat\":40.60425,\"lon\":-7.76115,\"timezone\":\"Europe/Lisbon\"," +
                "\"city_name\":\"Mangualde\",\"country_code\":\"PT\",\"state_code\":\"22\"," +
                "\"data\":[{\"aqi\":62,\"o3\":129.998,\"so2\":0.584871,\"no2\":0.532164,\"co\":305.831," +
                "\"pm25\":0.0953826,\"pm10\":0.433322,\"pollen_level_tree\":1.0,\"pollen_level_grass\":1.0," +
                "\"pollen_level_weed\":1.0,\"mold_level\":1,\"predominant_pollen_type\":\"Molds\"}]}";
    }

}
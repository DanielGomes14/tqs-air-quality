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
import tqs.airquality.models.AirQualityDataForecast;
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
    private static final String CURRENT_BASE_URI = "https://api.weatherbit.io/v2.0/current/airquality";
    private static final String FORECAST_BASE_URI = "https://api.weatherbit.io/v2.0/forecast/airquality";
    private static  final String API_TOKEN = "b425cc3001644d4bb6d9af3e4234d750";
    private static  final String CITY = "Mangualde";
    private static  final String INVALID_CITY = "UmaCidadeInvalida";
    private static final String COUNTRY ="PT";
    private static final String INVALID_COUNTRY ="FFFFF";

    @BeforeEach
    void setUp(){ }

    @Test
    void whenGetQualityByCityNameAndCountry_thenReturnCorrectQualityData() throws IOException, URISyntaxException, ParseException {
        String json_res = createJsonResponse();
        String final_current_url =  new URIHelper().buildURIWeatherBit(CURRENT_BASE_URI,API_TOKEN,CITY,COUNTRY).build().toString();
        when(httpClient.get(final_current_url)).thenReturn(json_res);

        AirQualityData json_obj = new ObjectMapper().readValue(json_res, AirQualityData.class);
        AirQualityData res_service = service.fetchAPIDataByCityNameAndCountry(CITY, COUNTRY).orElse(null);
        assertThat(res_service).isNotNull();
        assertThat(res_service.getCity_name(), is(json_obj.getCity_name()));
        assertThat(res_service,is(json_obj));
    }
    @Test
    void whenGetQualityByBadCityNameAndCountry_thenReturnIncorrectData() throws IOException, URISyntaxException, ParseException {
        String current_wrong_url = new URIHelper().buildURIWeatherBit(CURRENT_BASE_URI,API_TOKEN,CITY,INVALID_COUNTRY).build().toString();
        when(httpClient.get(current_wrong_url)).thenReturn(null);
        Optional<AirQualityData> res_service = service.fetchAPIDataByCityNameAndCountry(INVALID_CITY,COUNTRY);
        assertThat(res_service).isEmpty();
    }
    @Test
    void whenGetQualityByCityNameAndBadCountry_thenReturnIncorrectData() throws IOException, URISyntaxException, ParseException {
        String current_wrong_url_2 = new URIHelper().buildURIWeatherBit(CURRENT_BASE_URI,API_TOKEN,INVALID_CITY,COUNTRY).build().toString();
        when(httpClient.get(current_wrong_url_2)).thenReturn(null);
        Optional<AirQualityData> res_service = service.fetchAPIDataByCityNameAndCountry(CITY,INVALID_COUNTRY);
        assertThat(res_service).isEmpty();
    }
    @Test
    void whenGetQualityByBadCityNameAndBadCountry_thenReturnIncorrectData() throws IOException, URISyntaxException, ParseException {
        String current_wrong_url_3 = new URIHelper().buildURIWeatherBit(CURRENT_BASE_URI,API_TOKEN,INVALID_CITY,INVALID_COUNTRY).build().toString();
        when(httpClient.get(current_wrong_url_3)).thenReturn(null);
        Optional<AirQualityData> res_service = service.fetchAPIDataByCityNameAndCountry(INVALID_CITY,INVALID_COUNTRY);
        assertThat(res_service).isEmpty();
    }

    @Test
    void  whenGetForecastByValidCityNameAndCountry_thenReturnValidData() throws IOException, URISyntaxException, ParseException {
        String final_forecast_url =  new URIHelper().buildURIWeatherBit(FORECAST_BASE_URI,API_TOKEN,CITY,COUNTRY).build().toString();
        String json_res = createForecastJsonResponse();
        AirQualityDataForecast json_obj = new ObjectMapper().readValue(json_res, AirQualityDataForecast.class);
        when(httpClient.get(final_forecast_url)).thenReturn(json_res);
        AirQualityDataForecast res_service = service.fetchForecast(CITY,COUNTRY).orElse(null);
        assertThat(res_service).isNotNull();
        assertThat(res_service.getCity_name(), is(json_obj.getCity_name()));
        assertThat(res_service.getData().size(),is(3));
    }


    @Test
    void whenGetForecastByInvalidCityName_thenReturnIncorrectData() throws URISyntaxException, IOException, ParseException {
        String forecast_wrong_url = new URIHelper().buildURIWeatherBit(FORECAST_BASE_URI,API_TOKEN,INVALID_CITY,COUNTRY).build().toString();
        when(httpClient.get(forecast_wrong_url)).thenReturn(null);
        Optional<AirQualityDataForecast> res_service = service.fetchForecast(CITY,COUNTRY);
        assertThat(res_service).isEmpty();
    }
    @Test
    void whenGetForecastByInvalidCountry_thenReturnIncorrectData() throws URISyntaxException, IOException, ParseException {
        String forecast_wrong_url = new URIHelper().buildURIWeatherBit(FORECAST_BASE_URI,API_TOKEN,CITY,INVALID_COUNTRY).build().toString();
        when(httpClient.get(forecast_wrong_url)).thenReturn(null);
        Optional<AirQualityDataForecast> res_service = service.fetchForecast(CITY,INVALID_COUNTRY);
        assertThat(res_service).isEmpty();
    }
    @Test
    void whenGetForecastByInvalidCityNameAndCountry_thenReturnIncorrectData() throws URISyntaxException, IOException, ParseException {
        String forecast_wrong_url = new URIHelper().buildURIWeatherBit(FORECAST_BASE_URI,API_TOKEN,INVALID_CITY,INVALID_COUNTRY).build().toString();
        when(httpClient.get(forecast_wrong_url)).thenReturn(null);
        Optional<AirQualityDataForecast> res_service = service.fetchForecast(INVALID_CITY,INVALID_CITY);
        assertThat(res_service).isEmpty();
    }
    String createJsonResponse(){
        return "{\"lat\":40.60425,\"lon\":-7.76115,\"timezone\":\"Europe/Lisbon\"," +
                "\"city_name\":\"Mangualde\",\"country_code\":\"PT\",\"state_code\":\"22\"," +
                "\"data\":[{\"aqi\":62,\"o3\":129.998,\"so2\":0.584871,\"no2\":0.532164,\"co\":305.831," +
                "\"pm25\":0.0953826,\"pm10\":0.433322,\"pollen_level_tree\":1.0,\"pollen_level_grass\":1.0," +
                "\"pollen_level_weed\":1.0,\"mold_level\":1,\"predominant_pollen_type\":\"Molds\"}]}";
    }
    String createForecastJsonResponse(){
        return "{\"data\":[{\"aqi\":49,\"pm10\":20.006,\"pm25\":4.55646,\"o3\":105.143,\"timestamp_local\":\"2021-05-11T17:00:00\",\"so2\":0.709668,\"no2\":0.362808,\"timestamp_utc\":\"2021-05-11T16:00:00\",\"datetime\":\"2021-05-11:16\",\"co\":299.156,\"ts\":1620748800},{\"aqi\":49,\"pm10\":19.2606,\"pm25\":4.37794,\"o3\":105.572,\"timestamp_local\":\"2021-05-11T18:00:00\",\"so2\":0.669621,\"no2\":0.398152,\"timestamp_utc\":\"2021-05-11T17:00:00\",\"datetime\":\"2021-05-11:17\",\"co\":297.153,\"ts\":1620752400},{\"aqi\":49,\"pm10\":17.4605,\"pm25\":3.95109,\"o3\":106.287,\"timestamp_local\":\"2021-05-11T19:00:00\",\"so2\":0.623055,\"no2\":0.489189,\"timestamp_utc\":\"2021-05-11T18:00:00\",\"datetime\":\"2021-05-11:18\",\"co\":296.152,\"ts\":1620756000},{\"aqi\":49,\"pm10\":14.909,\"pm25\":3.36669,\"o3\":105.143,\"timestamp_local\":\"2021-05-11T20:00:00\",\"so2\":0.617467,\"no2\":0.915723,\"timestamp_utc\":\"2021-05-11T19:00:00\",\"datetime\":\"2021-05-11:19\",\"co\":296.235,\"ts\":1620759600},{\"aqi\":48,\"pm10\":14.2671,\"pm25\":3.2022,\"o3\":104.249,\"timestamp_local\":\"2021-05-11T21:00:00\",\"so2\":0.65472,\"no2\":1.45659,\"timestamp_utc\":\"2021-05-11T20:00:00\",\"datetime\":\"2021-05-11:20\",\"co\":302.076,\"ts\":1620763200},{\"aqi\":47,\"pm10\":14.1564,\"pm25\":3.15344,\"o3\":102.639,\"timestamp_local\":\"2021-05-11T22:00:00\",\"so2\":0.684522,\"no2\":1.75112,\"timestamp_utc\":\"2021-05-11T21:00:00\",\"datetime\":\"2021-05-11:21\",\"co\":305.414,\"ts\":1620766800},{\"aqi\":47,\"pm10\":14.5403,\"pm25\":3.2321,\"o3\":102.46,\"timestamp_local\":\"2021-05-11T23:00:00\",\"so2\":0.730157,\"no2\":1.85019,\"timestamp_utc\":\"2021-05-11T22:00:00\",\"datetime\":\"2021-05-11:22\",\"co\":311.255,\"ts\":1620770400},{\"aqi\":47,\"pm10\":14.7202,\"pm25\":3.30947,\"o3\":101.209,\"timestamp_local\":\"2021-05-12T00:00:00\",\"so2\":0.743195,\"no2\":1.76183,\"timestamp_utc\":\"2021-05-11T23:00:00\",\"datetime\":\"2021-05-11:23\",\"co\":312.507,\"ts\":1620774000},{\"aqi\":47,\"pm10\":15.1899,\"pm25\":3.50475,\"o3\":101.387,\"timestamp_local\":\"2021-05-12T01:00:00\",\"so2\":0.715256,\"no2\":1.55566,\"timestamp_utc\":\"2021-05-12T00:00:00\",\"datetime\":\"2021-05-12:00\",\"co\":318.766,\"ts\":1620777600},{\"aqi\":45,\"pm10\":8.19909,\"pm25\":2.92382,\"o3\":97.99,\"timestamp_local\":\"2021-05-12T02:00:00\",\"so2\":0.660308,\"no2\":1.30397,\"timestamp_utc\":\"2021-05-12T01:00:00\",\"datetime\":\"2021-05-12:01\",\"co\":318.766,\"ts\":1620781200},{\"aqi\":45,\"pm10\":3.68133,\"pm25\":1.79272,\"o3\":96.9172,\"timestamp_local\":\"2021-05-12T03:00:00\",\"so2\":0.656582,\"no2\":1.31736,\"timestamp_utc\":\"2021-05-12T02:00:00\",\"datetime\":\"2021-05-12:02\",\"co\":325.859,\"ts\":1620784800},{\"aqi\":45,\"pm10\":2.76735,\"pm25\":1.43076,\"o3\":96.5595,\"timestamp_local\":\"2021-05-12T04:00:00\",\"so2\":0.672415,\"no2\":1.35618,\"timestamp_utc\":\"2021-05-12T03:00:00\",\"datetime\":\"2021-05-12:03\",\"co\":325.024,\"ts\":1620788400},{\"aqi\":46,\"pm10\":1.49072,\"pm25\":0.877674,\"o3\":99.4205,\"timestamp_local\":\"2021-05-12T05:00:00\",\"so2\":0.655651,\"no2\":1.36957,\"timestamp_utc\":\"2021-05-12T04:00:00\",\"datetime\":\"2021-05-12:04\",\"co\":327.945,\"ts\":1620792000},{\"aqi\":45,\"pm10\":0.42173,\"pm25\":0.226025,\"o3\":97.096,\"timestamp_local\":\"2021-05-12T06:00:00\",\"so2\":0.603497,\"no2\":1.4914,\"timestamp_utc\":\"2021-05-12T05:00:00\",\"datetime\":\"2021-05-12:05\",\"co\":317.931,\"ts\":1620795600},{\"aqi\":44,\"pm10\":0.157825,\"pm25\":0.0461692,\"o3\":94.5926,\"timestamp_local\":\"2021-05-12T07:00:00\",\"so2\":0.542961,\"no2\":2.01084,\"timestamp_utc\":\"2021-05-12T06:00:00\",\"datetime\":\"2021-05-12:06\",\"co\":309.587,\"ts\":1620799200},{\"aqi\":43,\"pm10\":0.0603398,\"pm25\":0.00627413,\"o3\":92.8044,\"timestamp_local\":\"2021-05-12T08:00:00\",\"so2\":0.544824,\"no2\":2.61865,\"timestamp_utc\":\"2021-05-12T07:00:00\",\"datetime\":\"2021-05-12:07\",\"co\":304.163,\"ts\":1620802800},{\"aqi\":43,\"pm10\":0.115741,\"pm25\":0.0142036,\"o3\":92.6256,\"timestamp_local\":\"2021-05-12T09:00:00\",\"so2\":0.588596,\"no2\":2.63739,\"timestamp_utc\":\"2021-05-12T08:00:00\",\"datetime\":\"2021-05-12:08\",\"co\":302.911,\"ts\":1620806400},{\"aqi\":44,\"pm10\":0.243613,\"pm25\":0.0479217,\"o3\":94.9502,\"timestamp_local\":\"2021-05-12T10:00:00\",\"so2\":0.624917,\"no2\":2.30805,\"timestamp_utc\":\"2021-05-12T09:00:00\",\"datetime\":\"2021-05-12:09\",\"co\":304.997,\"ts\":1620810000},{\"aqi\":48,\"pm10\":1.36913,\"pm25\":0.495968,\"o3\":103.176,\"timestamp_local\":\"2021-05-12T11:00:00\",\"so2\":0.592321,\"no2\":1.37358,\"timestamp_utc\":\"2021-05-12T10:00:00\",\"datetime\":\"2021-05-12:10\",\"co\":312.507,\"ts\":1620813600},{\"aqi\":49,\"pm10\":5.32841,\"pm25\":2.17797,\"o3\":105.321,\"timestamp_local\":\"2021-05-12T12:00:00\",\"so2\":0.565313,\"no2\":0.76578,\"timestamp_utc\":\"2021-05-12T11:00:00\",\"datetime\":\"2021-05-12:11\",\"co\":302.494,\"ts\":1620817200},{\"aqi\":49,\"pm10\":9.01854,\"pm25\":3.4341,\"o3\":105.679,\"timestamp_local\":\"2021-05-12T13:00:00\",\"so2\":0.565313,\"no2\":0.500033,\"timestamp_utc\":\"2021-05-12T12:00:00\",\"datetime\":\"2021-05-12:12\",\"co\":295.818,\"ts\":1620820800},{\"aqi\":48,\"pm10\":14.8089,\"pm25\":4.65394,\"o3\":102.818,\"timestamp_local\":\"2021-05-12T14:00:00\",\"so2\":0.53551,\"no2\":0.372849,\"timestamp_utc\":\"2021-05-12T13:00:00\",\"datetime\":\"2021-05-12:13\",\"co\":288.725,\"ts\":1620824400},{\"aqi\":47,\"pm10\":25.6642,\"pm25\":6.63672,\"o3\":102.103,\"timestamp_local\":\"2021-05-12T15:00:00\",\"so2\":0.469852,\"no2\":0.323984,\"timestamp_utc\":\"2021-05-12T14:00:00\",\"datetime\":\"2021-05-12:14\",\"co\":293.314,\"ts\":1620828000},{\"aqi\":46,\"pm10\":24.0979,\"pm25\":5.79348,\"o3\":98.8841,\"timestamp_local\":\"2021-05-12T16:00:00\",\"so2\":0.446104,\"no2\":0.330008,\"timestamp_utc\":\"2021-05-12T15:00:00\",\"datetime\":\"2021-05-12:15\",\"co\":291.646,\"ts\":1620831600},{\"aqi\":46,\"pm10\":18.0403,\"pm25\":4.26406,\"o3\":98.8841,\"timestamp_local\":\"2021-05-12T17:00:00\",\"so2\":0.484288,\"no2\":0.388914,\"timestamp_utc\":\"2021-05-12T16:00:00\",\"datetime\":\"2021-05-12:16\",\"co\":294.149,\"ts\":1620835200},{\"aqi\":45,\"pm10\":13.3413,\"pm25\":3.17354,\"o3\":98.1689,\"timestamp_local\":\"2021-05-12T18:00:00\",\"so2\":0.59139,\"no2\":0.534841,\"timestamp_utc\":\"2021-05-12T17:00:00\",\"datetime\":\"2021-05-12:17\",\"co\":290.394,\"ts\":1620838800},{\"aqi\":46,\"pm10\":9.28553,\"pm25\":2.23165,\"o3\":99.957,\"timestamp_local\":\"2021-05-12T19:00:00\",\"so2\":0.653788,\"no2\":0.739004,\"timestamp_utc\":\"2021-05-12T18:00:00\",\"datetime\":\"2021-05-12:18\",\"co\":295.401,\"ts\":1620842400},{\"aqi\":45,\"pm10\":6.652,\"pm25\":1.62559,\"o3\":98.3477,\"timestamp_local\":\"2021-05-12T20:00:00\",\"so2\":0.747852,\"no2\":1.43249,\"timestamp_utc\":\"2021-05-12T19:00:00\",\"datetime\":\"2021-05-12:19\",\"co\":294.983,\"ts\":1620846000},{\"aqi\":45,\"pm10\":6.21322,\"pm25\":1.51157,\"o3\":97.99,\"timestamp_local\":\"2021-05-12T21:00:00\",\"so2\":0.786968,\"no2\":2.43657,\"timestamp_utc\":\"2021-05-12T20:00:00\",\"datetime\":\"2021-05-12:20\",\"co\":299.156,\"ts\":1620849600},{\"aqi\":45,\"pm10\":7.03492,\"pm25\":1.64041,\"o3\":96.7383,\"timestamp_local\":\"2021-05-12T22:00:00\",\"so2\":0.770204,\"no2\":3.02563,\"timestamp_utc\":\"2021-05-12T21:00:00\",\"datetime\":\"2021-05-12:21\",\"co\":301.242,\"ts\":1620853200},{\"aqi\":44,\"pm10\":8.07253,\"pm25\":1.89518,\"o3\":95.4866,\"timestamp_local\":\"2021-05-12T23:00:00\",\"so2\":0.555068,\"no2\":3.36836,\"timestamp_utc\":\"2021-05-12T22:00:00\",\"datetime\":\"2021-05-12:22\",\"co\":307.083,\"ts\":1620856800},{\"aqi\":43,\"pm10\":8.86635,\"pm25\":2.13851,\"o3\":92.4468,\"timestamp_local\":\"2021-05-13T00:00:00\",\"so2\":0.403263,\"no2\":3.66289,\"timestamp_utc\":\"2021-05-12T23:00:00\",\"datetime\":\"2021-05-12:23\",\"co\":310.838,\"ts\":1620860400},{\"aqi\":42,\"pm10\":9.61279,\"pm25\":2.37443,\"o3\":91.2845,\"timestamp_local\":\"2021-05-13T01:00:00\",\"so2\":0.382308,\"no2\":3.70038,\"timestamp_utc\":\"2021-05-13T00:00:00\",\"datetime\":\"2021-05-13:00\",\"co\":317.514,\"ts\":1620864000},{\"aqi\":42,\"pm10\":10.5992,\"pm25\":2.59048,\"o3\":90.1222,\"timestamp_local\":\"2021-05-13T02:00:00\",\"so2\":0.440516,\"no2\":3.43262,\"timestamp_utc\":\"2021-05-13T01:00:00\",\"datetime\":\"2021-05-13:01\",\"co\":320.852,\"ts\":1620867600},{\"aqi\":42,\"pm10\":12.0739,\"pm25\":2.83322,\"o3\":90.5693,\"timestamp_local\":\"2021-05-13T03:00:00\",\"so2\":0.538304,\"no2\":3.05776,\"timestamp_utc\":\"2021-05-13T02:00:00\",\"datetime\":\"2021-05-13:02\",\"co\":327.528,\"ts\":1620871200},{\"aqi\":42,\"pm10\":13.2809,\"pm25\":3.05332,\"o3\":90.9269,\"timestamp_local\":\"2021-05-13T04:00:00\",\"so2\":0.622123,\"no2\":2.6963,\"timestamp_utc\":\"2021-05-13T03:00:00\",\"datetime\":\"2021-05-13:03\",\"co\":331.7,\"ts\":1620874800},{\"aqi\":43,\"pm10\":14.3898,\"pm25\":3.29655,\"o3\":92.8044,\"timestamp_local\":\"2021-05-13T05:00:00\",\"so2\":0.678934,\"no2\":2.41515,\"timestamp_utc\":\"2021-05-13T04:00:00\",\"datetime\":\"2021-05-13:04\",\"co\":338.793,\"ts\":1620878400},{\"aqi\":44,\"pm10\":14.8894,\"pm25\":3.45885,\"o3\":94.9502,\"timestamp_local\":\"2021-05-13T06:00:00\",\"so2\":0.691041,\"no2\":2.15007,\"timestamp_utc\":\"2021-05-13T05:00:00\",\"datetime\":\"2021-05-13:05\",\"co\":340.879,\"ts\":1620882000},{\"aqi\":47,\"pm10\":14.3342,\"pm25\":3.39111,\"o3\":101.745,\"timestamp_local\":\"2021-05-13T07:00:00\",\"so2\":0.714324,\"no2\":2.20363,\"timestamp_utc\":\"2021-05-13T06:00:00\",\"datetime\":\"2021-05-13:06\",\"co\":349.641,\"ts\":1620885600},{\"aqi\":48,\"pm10\":11.9926,\"pm25\":2.85922,\"o3\":103.176,\"timestamp_local\":\"2021-05-13T08:00:00\",\"so2\":0.66869,\"no2\":2.04833,\"timestamp_utc\":\"2021-05-13T07:00:00\",\"datetime\":\"2021-05-13:07\",\"co\":340.044,\"ts\":1620889200},{\"aqi\":47,\"pm10\":14.4321,\"pm25\":3.40697,\"o3\":101.03,\"timestamp_local\":\"2021-05-13T09:00:00\",\"so2\":0.703149,\"no2\":2.20363,\"timestamp_utc\":\"2021-05-13T08:00:00\",\"datetime\":\"2021-05-13:08\",\"co\":338.793,\"ts\":1620892800},{\"aqi\":44,\"pm10\":21.6596,\"pm25\":5.13345,\"o3\":94.4138,\"timestamp_local\":\"2021-05-13T10:00:00\",\"so2\":0.785105,\"no2\":2.23308,\"timestamp_utc\":\"2021-05-13T09:00:00\",\"datetime\":\"2021-05-13:09\",\"co\":317.097,\"ts\":1620896400},{\"aqi\":45,\"pm10\":14.2941,\"pm25\":5.87651,\"o3\":98.3477,\"timestamp_local\":\"2021-05-13T11:00:00\",\"so2\":0.69011,\"no2\":1.5744,\"timestamp_utc\":\"2021-05-13T10:00:00\",\"datetime\":\"2021-05-13:10\",\"co\":304.163,\"ts\":1620900000},{\"aqi\":47,\"pm10\":3.59852,\"pm25\":2.40518,\"o3\":100.672,\"timestamp_local\":\"2021-05-13T12:00:00\",\"so2\":0.590459,\"no2\":1.13662,\"timestamp_utc\":\"2021-05-13T11:00:00\",\"datetime\":\"2021-05-13:11\",\"co\":289.559,\"ts\":1620903600},{\"aqi\":47,\"pm10\":0.983482,\"pm25\":0.673165,\"o3\":102.46,\"timestamp_local\":\"2021-05-13T13:00:00\",\"so2\":0.562519,\"no2\":0.868866,\"timestamp_utc\":\"2021-05-13T12:00:00\",\"datetime\":\"2021-05-13:12\",\"co\":280.797,\"ts\":1620907200},{\"aqi\":50,\"pm10\":0.33847,\"pm25\":0.173585,\"o3\":107.288,\"timestamp_local\":\"2021-05-13T14:00:00\",\"so2\":0.506639,\"no2\":0.713568,\"timestamp_utc\":\"2021-05-13T13:00:00\",\"datetime\":\"2021-05-13:13\",\"co\":278.711,\"ts\":1620910800},{\"aqi\":52,\"pm10\":0.27502,\"pm25\":0.120779,\"o3\":111.759,\"timestamp_local\":\"2021-05-13T15:00:00\",\"so2\":0.517815,\"no2\":0.589731,\"timestamp_utc\":\"2021-05-13T14:00:00\",\"datetime\":\"2021-05-13:14\",\"co\":276.208,\"ts\":1620914400},{\"aqi\":53,\"pm10\":0.324124,\"pm25\":0.155731,\"o3\":114.62,\"timestamp_local\":\"2021-05-13T16:00:00\",\"so2\":0.507571,\"no2\":0.504719,\"timestamp_utc\":\"2021-05-13T15:00:00\",\"datetime\":\"2021-05-13:15\",\"co\":277.877,\"ts\":1620918000},{\"aqi\":53,\"pm10\":1.72896,\"pm25\":0.699316,\"o3\":114.977,\"timestamp_local\":\"2021-05-13T17:00:00\",\"so2\":0.590459,\"no2\":0.499364,\"timestamp_utc\":\"2021-05-13T16:00:00\",\"datetime\":\"2021-05-13:16\",\"co\":284.135,\"ts\":1620921600},{\"aqi\":51,\"pm10\":10.5417,\"pm25\":3.18938,\"o3\":110.686,\"timestamp_local\":\"2021-05-13T18:00:00\",\"so2\":0.642613,\"no2\":0.615168,\"timestamp_utc\":\"2021-05-13T17:00:00\",\"datetime\":\"2021-05-13:17\",\"co\":282.884,\"ts\":1620925200},{\"aqi\":51,\"pm10\":15.0806,\"pm25\":4.28506,\"o3\":109.613,\"timestamp_local\":\"2021-05-13T19:00:00\",\"so2\":0.686385,\"no2\":0.858156,\"timestamp_utc\":\"2021-05-13T18:00:00\",\"datetime\":\"2021-05-13:18\",\"co\":288.725,\"ts\":1620928800},{\"aqi\":50,\"pm10\":7.10859,\"pm25\":1.99148,\"o3\":107.288,\"timestamp_local\":\"2021-05-13T20:00:00\",\"so2\":0.8028,\"no2\":1.45659,\"timestamp_utc\":\"2021-05-13T19:00:00\",\"datetime\":\"2021-05-13:19\",\"co\":289.142,\"ts\":1620932400},{\"aqi\":50,\"pm10\":2.44976,\"pm25\":0.635659,\"o3\":107.288,\"timestamp_local\":\"2021-05-13T21:00:00\",\"so2\":0.827014,\"no2\":2.2304,\"timestamp_utc\":\"2021-05-13T20:00:00\",\"datetime\":\"2021-05-13:20\",\"co\":294.566,\"ts\":1620936000},{\"aqi\":48,\"pm10\":3.53124,\"pm25\":0.828079,\"o3\":104.785,\"timestamp_local\":\"2021-05-13T22:00:00\",\"so2\":0.777654,\"no2\":2.52493,\"timestamp_utc\":\"2021-05-13T21:00:00\",\"datetime\":\"2021-05-13:21\",\"co\":295.818,\"ts\":1620939600},{\"aqi\":49,\"pm10\":5.45313,\"pm25\":1.20249,\"o3\":105.858,\"timestamp_local\":\"2021-05-13T23:00:00\",\"so2\":0.692904,\"no2\":2.57848,\"timestamp_utc\":\"2021-05-13T22:00:00\",\"datetime\":\"2021-05-13:22\",\"co\":308.335,\"ts\":1620943200},{\"aqi\":49,\"pm10\":8.94533,\"pm25\":1.93725,\"o3\":105.5,\"timestamp_local\":\"2021-05-14T00:00:00\",\"so2\":0.610016,\"no2\":2.54635,\"timestamp_utc\":\"2021-05-13T23:00:00\",\"datetime\":\"2021-05-13:23\",\"co\":316.262,\"ts\":1620946800},{\"aqi\":50,\"pm10\":9.73524,\"pm25\":2.10925,\"o3\":108.182,\"timestamp_local\":\"2021-05-14T01:00:00\",\"so2\":0.564381,\"no2\":2.41515,\"timestamp_utc\":\"2021-05-14T00:00:00\",\"datetime\":\"2021-05-14:00\",\"co\":331.283,\"ts\":1620950400},{\"aqi\":50,\"pm10\":9.19738,\"pm25\":2.01593,\"o3\":107.825,\"timestamp_local\":\"2021-05-14T02:00:00\",\"so2\":0.4787,\"no2\":2.2304,\"timestamp_utc\":\"2021-05-14T01:00:00\",\"datetime\":\"2021-05-14:01\",\"co\":334.62,\"ts\":1620954000},{\"aqi\":49,\"pm10\":8.72843,\"pm25\":1.93167,\"o3\":106.752,\"timestamp_local\":\"2021-05-14T03:00:00\",\"so2\":0.414904,\"no2\":2.10188,\"timestamp_utc\":\"2021-05-14T02:00:00\",\"datetime\":\"2021-05-14:02\",\"co\":336.707,\"ts\":1620957600},{\"aqi\":48,\"pm10\":8.24919,\"pm25\":1.83092,\"o3\":103.891,\"timestamp_local\":\"2021-05-14T04:00:00\",\"so2\":0.396278,\"no2\":1.96265,\"timestamp_utc\":\"2021-05-14T03:00:00\",\"datetime\":\"2021-05-14:03\",\"co\":332.117,\"ts\":1620961200},{\"aqi\":47,\"pm10\":7.91299,\"pm25\":1.7635,\"o3\":101.387,\"timestamp_local\":\"2021-05-14T05:00:00\",\"so2\":0.393949,\"no2\":1.87964,\"timestamp_utc\":\"2021-05-14T04:00:00\",\"datetime\":\"2021-05-14:04\",\"co\":328.362,\"ts\":1620964800},{\"aqi\":45,\"pm10\":7.63307,\"pm25\":1.72066,\"o3\":98.1689,\"timestamp_local\":\"2021-05-14T06:00:00\",\"so2\":0.409782,\"no2\":1.86358,\"timestamp_utc\":\"2021-05-14T05:00:00\",\"datetime\":\"2021-05-14:05\",\"co\":323.355,\"ts\":1620968400},{\"aqi\":44,\"pm10\":7.51797,\"pm25\":1.7282,\"o3\":95.4866,\"timestamp_local\":\"2021-05-14T07:00:00\",\"so2\":0.428408,\"no2\":2.34018,\"timestamp_utc\":\"2021-05-14T06:00:00\",\"datetime\":\"2021-05-14:06\",\"co\":320.435,\"ts\":1620972000},{\"aqi\":43,\"pm10\":7.57699,\"pm25\":1.76888,\"o3\":92.6256,\"timestamp_local\":\"2021-05-14T08:00:00\",\"so2\":0.508502,\"no2\":3.09525,\"timestamp_utc\":\"2021-05-14T07:00:00\",\"datetime\":\"2021-05-14:07\",\"co\":317.097,\"ts\":1620975600},{\"aqi\":42,\"pm10\":7.95435,\"pm25\":1.8715,\"o3\":91.2845,\"timestamp_local\":\"2021-05-14T09:00:00\",\"so2\":0.696629,\"no2\":3.46475,\"timestamp_utc\":\"2021-05-14T08:00:00\",\"datetime\":\"2021-05-14:08\",\"co\":315.845,\"ts\":1620979200},{\"aqi\":44,\"pm10\":8.08545,\"pm25\":1.93058,\"o3\":95.8443,\"timestamp_local\":\"2021-05-14T10:00:00\",\"so2\":0.976026,\"no2\":2.94531,\"timestamp_utc\":\"2021-05-14T09:00:00\",\"datetime\":\"2021-05-14:09\",\"co\":310.838,\"ts\":1620982800},{\"aqi\":54,\"pm10\":4.75933,\"pm25\":1.27671,\"o3\":116.05,\"timestamp_local\":\"2021-05-14T11:00:00\",\"so2\":0.722706,\"no2\":0.938482,\"timestamp_utc\":\"2021-05-14T10:00:00\",\"datetime\":\"2021-05-14:10\",\"co\":312.507,\"ts\":1620986400},{\"aqi\":52,\"pm10\":4.41601,\"pm25\":1.18553,\"o3\":121.593,\"timestamp_local\":\"2021-05-14T12:00:00\",\"so2\":0.595115,\"no2\":0.549568,\"timestamp_utc\":\"2021-05-14T11:00:00\",\"datetime\":\"2021-05-14:11\",\"co\":314.176,\"ts\":1620990000},{\"aqi\":55,\"pm10\":5.29431,\"pm25\":1.3886,\"o3\":124.097,\"timestamp_local\":\"2021-05-14T13:00:00\",\"so2\":0.589527,\"no2\":0.417698,\"timestamp_utc\":\"2021-05-14T12:00:00\",\"datetime\":\"2021-05-14:12\",\"co\":314.593,\"ts\":1620993600},{\"aqi\":53,\"pm10\":6.66968,\"pm25\":1.67488,\"o3\":122.488,\"timestamp_local\":\"2021-05-14T14:00:00\",\"so2\":0.583939,\"no2\":0.356784,\"timestamp_utc\":\"2021-05-14T13:00:00\",\"datetime\":\"2021-05-14:13\",\"co\":306.666,\"ts\":1620997200},{\"aqi\":51,\"pm10\":8.27875,\"pm25\":1.99228,\"o3\":120.878,\"timestamp_local\":\"2021-05-14T15:00:00\",\"so2\":0.622123,\"no2\":0.355445,\"timestamp_utc\":\"2021-05-14T14:00:00\",\"datetime\":\"2021-05-14:14\",\"co\":300.825,\"ts\":1621000800},{\"aqi\":54,\"pm10\":9.15229,\"pm25\":2.15723,\"o3\":117.123,\"timestamp_local\":\"2021-05-14T16:00:00\",\"so2\":0.643544,\"no2\":0.37218,\"timestamp_utc\":\"2021-05-14T15:00:00\",\"datetime\":\"2021-05-14:15\",\"co\":291.228,\"ts\":1621004400}],\"city_name\":\"Mangualde\",\"lon\":\"-7.76115\",\"timezone\":\"Europe\\/Lisbon\",\"lat\":\"40.60425\",\"country_code\":\"PT\",\"state_code\":\"22\"}";
    }

}
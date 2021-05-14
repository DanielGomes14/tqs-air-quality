package tqs.airquality.models;

import java.util.List;
import lombok.Data;

@Data
public class AirQualityDataForecast {
    private double lat;
    private double lon;
    private String timezone;
    private String city_name;
    private String country_code;
    private String state_code;
    private List<Metrics> data;
}

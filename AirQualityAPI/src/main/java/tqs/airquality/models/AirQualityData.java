package tqs.airquality.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQualityData {
    private double lat;
    private double lon;
    private String timezone;
    private String city_name;
    private String country_code;
    private String state_code;
    private Metrics[] data;

}

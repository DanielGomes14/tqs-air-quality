package tqs.airquality.models;


import lombok.Data;


@Data
public class AirQualityData {
    private double lat;
    private double lon;
    private String timezone;
    private String country_code;
    private String state_code;
    private Particles[] pollutants;

}

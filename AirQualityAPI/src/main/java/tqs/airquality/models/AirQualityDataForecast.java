package tqs.airquality.models;


import lombok.Data;

@Data
public class AirQualityDataForecast {
    private double lat;
    private double lon;
    private Particles[] data;
}

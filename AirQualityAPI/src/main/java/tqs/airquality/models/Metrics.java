package tqs.airquality.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metrics {
    private int aqi;
    private double o3;
    private double so2;
    private double no2;
    private double co;
    private double pm25;
    private double pm10;
}

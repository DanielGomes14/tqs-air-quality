package tqs.airquality.models;

import java.util.List;
import lombok.Data;

@Data
public class AirQualityDataForecast {
    private double lat;
    private double lon;
    private List<Particles> data;
}

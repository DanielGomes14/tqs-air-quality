package tqs.airquality.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Data
@AllArgsConstructor
public class City {
    @Id
    private long cityid;
    private String cityname;
    private String statecode;
    private String countrycode;
    private String countyfull;
    private double lat;
    private double lon;

    public City() {
        super();
    }
}

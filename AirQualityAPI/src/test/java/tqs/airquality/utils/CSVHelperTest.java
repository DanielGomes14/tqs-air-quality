package tqs.airquality.utils;

import org.junit.jupiter.api.Test;
import tqs.airquality.models.City;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
class CSVHelperTest {

    private static final String VALID_FILENAME ="cities_20000.csv";
    private static final String INVALID_FILENAME ="citiesssss.csv";

    private final CSVHelper csvHelper = new CSVHelper();
    @Test
    void whenValidFileName_thenReturnListOfCities(){
       List<City> cities =  csvHelper.readCSVDataToArray(VALID_FILENAME);
        assertThat(cities)
                .isNotNull()
                .isInstanceOf(List.class)
                .extracting(City::getCityname).isNotEmpty();
    }

    @Test
    void whenInvalidFileName_thenReturnEmptyListOfCities(){
        List<City> cities =  csvHelper.readCSVDataToArray(INVALID_FILENAME);
        assertThat(cities).isInstanceOf(List.class).isEmpty();

    }
}

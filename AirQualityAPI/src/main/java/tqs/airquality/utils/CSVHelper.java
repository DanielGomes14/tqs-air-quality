package tqs.airquality.utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tqs.airquality.models.City;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {
    private static final Logger logger = LogManager.getLogger(CSVHelper.class);


    public  List<City> readCSVDataToArray(String fileName){
        List<City> citiesdata = new ArrayList<>();

        try (var br = new BufferedReader(new FileReader(fileName))) {
            String line;
            var counter = 0;
            while ((line = br.readLine()) != null) {
                 counter++;
                if(counter == 1) continue;
                String[] citysep = line.split(",");
                if(citysep.length == 7){
                    City city = new City(
                            Long.parseLong(citysep[0]),
                            citysep[1],
                            citysep[2],
                            citysep[3],
                            citysep[4],
                            Double.parseDouble(citysep[5]),
                            Double.parseDouble(citysep[6]));
                    citiesdata.add(city);
                }
            }
            return citiesdata;
        } catch( IOException e) {
            logger.warn("Could not Parse Cities List");
        }
        return citiesdata;

    }

}

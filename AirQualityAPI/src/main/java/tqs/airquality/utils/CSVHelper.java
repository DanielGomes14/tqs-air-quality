package tqs.airquality.utils;


import tqs.airquality.models.City;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public  List<City> readCSVDataToArray(String fileName){
        List<City> citiesdata = new ArrayList<>();

        try (var br = new BufferedReader(new FileReader(fileName))) {
            String line;
            var counter = 0;
            while ((line = br.readLine()) != null) {
                 counter++;
                if(counter == 1) continue;;
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
                e.printStackTrace();
        }
        return citiesdata;

    }

}

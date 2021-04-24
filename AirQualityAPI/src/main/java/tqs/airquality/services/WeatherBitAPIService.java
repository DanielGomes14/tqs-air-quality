package tqs.airquality.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WeatherBitAPIService {
    @Value("${weatherbit_token}")
    private String weather_bit_token;

    @Autowired
    private BasicHttpClient basicHttpClient;


}

package tqs.airquality.services;

import java.io.IOException;

public interface IHttpClient {
    public String get(String url) throws IOException;
}

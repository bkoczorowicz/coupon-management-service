package pl.koczorowicz.empik.service;

import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

public interface GeolocationService {

    String getCountryByIp(String ip) throws HttpException;

}

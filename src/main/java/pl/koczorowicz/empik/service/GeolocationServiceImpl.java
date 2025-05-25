package pl.koczorowicz.empik.service;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.koczorowicz.empik.dto.GeolocationDataDto;


@Service
public class GeolocationServiceImpl implements GeolocationService {

    private static final String IP_API_BASE_URL = "http://ip-api.com";
    private RestTemplate restTemplate;

    public GeolocationServiceImpl(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getCountryByIp(String ip) throws HttpException {
        String url = UriComponentsBuilder
                .fromHttpUrl(IP_API_BASE_URL)
                .pathSegment("json", ip)
                .toUriString();
        ResponseEntity<GeolocationDataDto> response = restTemplate.getForEntity(url, GeolocationDataDto.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpException("Failed to get geolocation data for IP: " + ip);
        }
        if (response.getBody() == null) {
            throw new HttpException("Empty response body for IP: " + ip);
        }
        return response.getBody().getCountryCode();
    }
}

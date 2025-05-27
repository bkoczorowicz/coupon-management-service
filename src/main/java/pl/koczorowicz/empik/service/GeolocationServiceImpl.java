package pl.koczorowicz.empik.service;

import org.apache.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.koczorowicz.empik.dto.GeolocationDataDto;


@Service
public class GeolocationServiceImpl implements GeolocationService {

    private String ipApiBaseUrl;
    private RestTemplate restTemplate;

    public GeolocationServiceImpl(@Autowired RestTemplate restTemplate, @Value("${ip.api.url}") String ipApiBaseUrl) {
        this.restTemplate = restTemplate;
        this.ipApiBaseUrl = ipApiBaseUrl;
    }

    @Override
    public String getCountryByIp(String ip) throws HttpException {
        String url = UriComponentsBuilder
                .fromHttpUrl(ipApiBaseUrl)
                .pathSegment("json", ip)
                .toUriString();
        ResponseEntity<GeolocationDataDto> response = restTemplate.getForEntity(url, GeolocationDataDto.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new HttpException("Failed to get geolocation data for IP: " + ip);
        }
        if (response.getBody() == null || response.getBody().getCountryCode() == null) {
            throw new HttpException("Geolocation data is missing for IP: " + ip);
        }
        return response.getBody().getCountryCode();
    }


}

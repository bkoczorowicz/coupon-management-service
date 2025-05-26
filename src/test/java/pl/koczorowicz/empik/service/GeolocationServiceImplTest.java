package pl.koczorowicz.empik.service;

import org.apache.http.HttpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import pl.koczorowicz.empik.dto.GeolocationDataDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
public class GeolocationServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @Value("${ip.api.url}")
    private String ipApiBaseUrl;

    @InjectMocks
    private GeolocationServiceImpl testedObject;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        testedObject = new GeolocationServiceImpl(restTemplate, ipApiBaseUrl);
    }

    @Test
    void shouldReturnCorrectCountryCodeWhenRestTemplateReturnsHTTP200() throws HttpException {
        // Given
        GeolocationDataDto dataStub = new GeolocationDataDto();
        dataStub.setCountryCode("PL");
        when(restTemplate.getForEntity(anyString(), eq(GeolocationDataDto.class))).thenReturn(new ResponseEntity<>(dataStub, HttpStatus.OK));

        // When
        String countryCode = testedObject.getCountryByIp("0.0.0.0");

        // Then
        assertEquals("PL", countryCode);
    }

    @Test
    void shouldThrowAnExceptionWhenResponseEntityIsNotOk() throws HttpException {

        // Given
        when(restTemplate.getForEntity(anyString(), eq(GeolocationDataDto.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        // When & Then
        Exception exception = assertThrows(HttpException.class, () -> {
            testedObject.getCountryByIp("0.0.0.0");
        });

        String expectedMessage = "Failed to get geolocation data for IP: 0.0.0.0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void shouldThrowAnExceptionWhenResponseEntityIsEmpty() throws HttpException {
        // Given
        when(restTemplate.getForEntity(anyString(), eq(GeolocationDataDto.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // When & Then
        Exception exception = assertThrows(HttpException.class, () -> {
            testedObject.getCountryByIp("0.0.0.0");
        });

        String expectedMessage = "Geolocation data is missing for IP: 0.0.0.0";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
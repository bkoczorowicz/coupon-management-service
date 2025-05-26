package pl.koczorowicz.empik.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
public class IpAddressDeterminerTest {

    @Value("${localhost.public-ip-check.url}")
    private String checkIpAddress;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IpAddressDeterminer testedObject;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        testedObject = new IpAddressDeterminer(checkIpAddress, restTemplate);
    }

    @Test
    public void shouldGetAddressFromRequestHeaderWhenAvailable() {
        // Given
        String expectedIp = "1.2.3.4";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(expectedIp);

        // when
        String actualIp = testedObject.determineClientIpAddress(request);

        // then
        assertEquals(expectedIp, actualIp);
    }

    @Test
    public void shouldGetPublicIpAddressForLocalhostIfNoHeaderHasAddress() {
        // Given
        String expectedIp = "1.2.3.4";
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("0:0:0:0:0:0:0:1");
        when(restTemplate.getForObject(checkIpAddress, String.class)).thenReturn(expectedIp);
        // when
        String actualIp = testedObject.determineClientIpAddress(request);

        // then
        assertEquals(expectedIp, actualIp);

    }

}
package pl.koczorowicz.empik.facade;

import org.apache.http.HttpException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.koczorowicz.empik.exception.CouponAlreadyUsedException;
import pl.koczorowicz.empik.exception.CouponNotValidForCountryException;
import pl.koczorowicz.empik.model.Coupon;
import pl.koczorowicz.empik.service.CouponService;
import pl.koczorowicz.empik.service.GeolocationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
public class CouponManagementFacadeTest {

    @Mock
    private CouponService couponService;

    @Mock
    private GeolocationService geolocationService;

    @InjectMocks
    private CouponManagementFacade testedObject;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        testedObject = new CouponManagementFacade(couponService, geolocationService);
    }

    @Test
    public void shouldSuccessfullyRedeemCouponWhenUserCountryAndCouponCountryMatch() throws HttpException, CouponAlreadyUsedException, CouponNotValidForCountryException {
        // Given
        String code = "TESTCOUPON";
        String ipAddress = "1.2.3.4";
        Coupon expectedCoupon = new Coupon();
        expectedCoupon.setCountries(List.of("PL", "US"));

        when(couponService.getCouponByCode("TESTCOUPON")).thenReturn(expectedCoupon);
        when(geolocationService.getCountryByIp(ipAddress)).thenReturn("PL");
        when(couponService.useCoupon(code)).thenReturn(expectedCoupon);

        // When
        Coupon redeemedCoupon = testedObject.redeemCoupon(code, ipAddress);

        // Then
        assertNotNull(redeemedCoupon);
    }

    @Test
    public void shouldNotRedeemCouponWhenUserCountryAndCouponCountryDoNotMatch() throws HttpException, CouponAlreadyUsedException, CouponNotValidForCountryException {
        // Given
        String code = "TESTCOUPON";
        String ipAddress = "1.2.3.4";
        Coupon expectedCoupon = new Coupon();
        expectedCoupon.setCountries(List.of("PL", "US"));

        when(couponService.getCouponByCode("TESTCOUPON")).thenReturn(expectedCoupon);
        when(geolocationService.getCountryByIp(ipAddress)).thenReturn("DE");
        when(couponService.useCoupon(code)).thenReturn(expectedCoupon);

        // When
        Exception exception = assertThrows(CouponNotValidForCountryException.class,
                () -> testedObject.redeemCoupon(code, ipAddress));

        // Then
        String expectedMessage = "Coupon \"" + code +  "\" is not valid in your country";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
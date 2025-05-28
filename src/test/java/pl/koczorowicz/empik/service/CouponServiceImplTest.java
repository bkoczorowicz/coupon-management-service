package pl.koczorowicz.empik.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.koczorowicz.empik.exception.CouponAlreadyUsedException;
import pl.koczorowicz.empik.model.Coupon;
import pl.koczorowicz.empik.repository.CouponRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestPropertySource("classpath:application-test.properties")
@ExtendWith(SpringExtension.class)
public class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl testedObject;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
        testedObject = new CouponServiceImpl(couponRepository);
    }

    @Test
    public void shouldProperlyUseCoupon() throws CouponAlreadyUsedException {
        // Given
        String code = "TESTCOUPON";
        String userName = "testUser";
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setRemainingUses(5);
        coupon.setUsedAlreadyBy(new ArrayList<>());

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(coupon)).thenReturn(coupon);

        // When
        Coupon usedCoupon = testedObject.useCoupon(code, userName);

        // Then
        assertEquals(4, usedCoupon.getRemainingUses());
    }

    @Test
    public void shouldThrowExceptionWhenCouponHasNoRemainingUses() {
        // Given
        String code = "TESTCOUPON";
        String userName = "testUser";
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setRemainingUses(0);
        coupon.setUsedAlreadyBy(new ArrayList<>());

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.of(coupon));

        // When
        Exception exception = assertThrows(CouponAlreadyUsedException.class, () -> testedObject.useCoupon(code, userName));

        // Then
        String expectedMessage = "Coupon \"" + code + "\" has no remaining uses";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    public void shouldThrowExceptionWhenCouponNotFound() {
        // Given
        String code = "NONEXISTENTCOUPON";
        String userName = "testUser";


        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(NoSuchElementException.class, () -> testedObject.useCoupon(code, userName));

    }

    @Test
    public void shouldThrowExceptionWhenUserHasAlreadyUsedCouponOnce() {
        // Given
        String code = "TESTCOUPON";
        String userName = "testUser";
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setRemainingUses(5);
        coupon.setUsedAlreadyBy(Arrays.asList(userName));

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(coupon)).thenReturn(coupon);

        // When
        Exception exception = assertThrows(CouponAlreadyUsedException.class, () -> testedObject.useCoupon(code, userName));

        // Then
        String expectedMessage = "Coupon \"" + code + "\" has already been used by user: " + userName;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }


}
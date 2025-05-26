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
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setRemainingUses(5);

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.of(coupon));
        when(couponRepository.save(coupon)).thenReturn(coupon);

        // When
        Coupon usedCoupon = testedObject.useCoupon(code);

        // Then
        assertEquals(4, usedCoupon.getRemainingUses());
    }

    @Test
    public void shouldThrowExceptionWhenCouponHasNoRemainingUses() {
        // Given
        String code = "TESTCOUPON";
        Coupon coupon = new Coupon();
        coupon.setCode(code);
        coupon.setRemainingUses(0);

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.of(coupon));

        // When & Then
        assertThrows(CouponAlreadyUsedException.class, () -> testedObject.useCoupon(code));
    }

    @Test
    public void shouldThrowExceptionWhenCouponNotFound() {
        // Given
        String code = "NONEXISTENTCOUPON";

        when(couponRepository.findByCodeIgnoreCase(code)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchElementException.class, () -> testedObject.useCoupon(code));
    }

}
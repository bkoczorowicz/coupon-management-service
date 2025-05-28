package pl.koczorowicz.empik.service;

import org.springframework.stereotype.Service;
import pl.koczorowicz.empik.exception.CouponAlreadyUsedException;
import pl.koczorowicz.empik.model.Coupon;

import java.util.UUID;

public interface CouponService {

    Coupon getCouponByCode(String code);

    Coupon createOrUpdateCoupon(Coupon coupon);

    void deleteCoupon(String code);

    Coupon useCoupon(String code, String userName) throws CouponAlreadyUsedException;
}

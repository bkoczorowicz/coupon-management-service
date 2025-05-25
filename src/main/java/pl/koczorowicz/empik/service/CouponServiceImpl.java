package pl.koczorowicz.empik.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.koczorowicz.empik.model.Coupon;
import pl.koczorowicz.empik.repository.CouponRepository;

@Service
public class CouponServiceImpl implements CouponService {

    private CouponRepository couponRepository;

    public CouponServiceImpl(@Autowired CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCodeIgnoreCase(code).orElseThrow();
    }

    public Coupon createOrUpdateCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public void deleteCoupon(String code) {
        Coupon coupon = couponRepository.findByCodeIgnoreCase(code).orElseThrow();
        couponRepository.delete(coupon);
    }

    public Coupon useCoupon(String code) {
        Coupon coupon = couponRepository.findByCodeIgnoreCase(code).orElseThrow();
        if (coupon.getRemainingUses() <= 0) {
            throw new IllegalStateException("Coupon has no remaining uses");
        }
        coupon.setRemainingUses(coupon.getRemainingUses() - 1);
        return couponRepository.save(coupon);
    }

}

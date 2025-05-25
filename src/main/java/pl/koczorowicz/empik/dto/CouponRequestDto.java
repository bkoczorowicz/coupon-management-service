package pl.koczorowicz.empik.dto;

import pl.koczorowicz.empik.model.Coupon;

import java.time.LocalDateTime;
import java.util.List;

public class CouponRequestDto {
    private String code;
    private int maxUses;
    private List<String> countries;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public static Coupon toNewCoupon(CouponRequestDto couponDto) {
        Coupon coupon = new Coupon();
        coupon.setCode(couponDto.getCode());
        coupon.setCreationDate(LocalDateTime.now());
        coupon.setMaxUses(couponDto.getMaxUses());
        coupon.setRemainingUses(couponDto.getMaxUses());
        coupon.setCountries(couponDto.getCountries());
        return coupon;
    }
}

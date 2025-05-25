package pl.koczorowicz.empik.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.koczorowicz.empik.model.Coupon;

import java.time.LocalDateTime;
import java.util.List;

public class CouponResponseDto {

    private String code;
    private LocalDateTime creationDate;
    private int maxUses;
    private int remainingUses;
    private List<String> countries;

    public CouponResponseDto() {
    }

    public CouponResponseDto(String code, int maxUses, List<String> countries) {
        this.code = code;
        this.maxUses = maxUses;
        this.remainingUses = maxUses;
        this.countries = countries;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int getRemainingUses() {
        return remainingUses;
    }

    public void setRemainingUses(int remainingUses) {
        this.remainingUses = remainingUses;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public static CouponResponseDto fromCoupon(Coupon coupon) {
        CouponResponseDto couponDto = new CouponResponseDto();
        couponDto.setCode(coupon.getCode());
        couponDto.setCreationDate(coupon.getCreationDate());
        couponDto.setMaxUses(coupon.getMaxUses());
        couponDto.setRemainingUses(coupon.getRemainingUses());
        couponDto.setCountries(coupon.getCountries());
        return couponDto;
    }

    @Override
    public String toString() {
        return "CouponDto{" +
                ", code='" + code + '\'' +
                ", creationDate=" + creationDate +
                ", maxUses=" + maxUses +
                ", currentUses=" + remainingUses +
                ", countries=" + countries +
                '}';
    }
}

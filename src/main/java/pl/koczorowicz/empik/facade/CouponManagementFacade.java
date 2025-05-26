package pl.koczorowicz.empik.facade;

import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.koczorowicz.empik.exception.CouponAlreadyUsedException;
import pl.koczorowicz.empik.exception.CouponNotValidForCountryException;
import pl.koczorowicz.empik.model.Coupon;
import pl.koczorowicz.empik.service.CouponService;
import pl.koczorowicz.empik.service.GeolocationService;

import java.util.List;

@Component
public class CouponManagementFacade {

    Logger logger = LoggerFactory.getLogger(CouponManagementFacade.class);

    private CouponService couponService;
    private GeolocationService geolocationService;

    public CouponManagementFacade(@Autowired CouponService couponService, @Autowired GeolocationService geolocationService) {
        this.couponService = couponService;
        this.geolocationService = geolocationService;
    }

    public Coupon createNewCoupon(Coupon coupon) {
        logger.info("Creating new coupon: {}", coupon);
        return couponService.createOrUpdateCoupon(coupon);
    }

    public void deleteCoupon(String code) {
        logger.info("Deleting coupon with code: {}", code);
        couponService.deleteCoupon(code);
    }

    public Coupon redeemCoupon(String code, String ipAddress) throws HttpException, CouponNotValidForCountryException, CouponAlreadyUsedException {
        logger.info("Redeeming coupon with code: {}", code);
        Coupon coupon = couponService.getCouponByCode(code);
        List<String> elligibleCountries = coupon.getCountries();
        String userCountry = geolocationService.getCountryByIp(ipAddress);
        if (!elligibleCountries.contains(userCountry)) {
            throw new CouponNotValidForCountryException("Coupon \"" + code +  "\" is not valid in your country");
        }
        return couponService.useCoupon(code);
    }

    public Coupon getCouponByCode(String code) {
        logger.info("Retrieving coupon with code: {}", code);
        return couponService.getCouponByCode(code);
    }
}

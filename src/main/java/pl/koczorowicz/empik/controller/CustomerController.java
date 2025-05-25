package pl.koczorowicz.empik.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.koczorowicz.empik.dto.CouponResponseDto;
import pl.koczorowicz.empik.exception.CouponAlreadyUsedException;
import pl.koczorowicz.empik.exception.CouponNotValidForCountryException;
import pl.koczorowicz.empik.facade.CouponManagementFacade;
import pl.koczorowicz.empik.utils.IpAddressDeterminer;
import java.util.Map;


@RestController
@RequestMapping("/customer")
public class CustomerController {

    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CouponManagementFacade couponManagementFacade;

    @Autowired
    private IpAddressDeterminer ipAddressDeterminer;

    /**
     *
     * This endpoint should be used to redeem a coupon, which boils down to
     * checking if the coupon is valid and has remaining uses.
     * If the coupon is valid, it will be redeemed and the remaining uses will be decremented.
     *
     * @return
     */
    @PostMapping("/coupon/{code}/remaining-uses")
    public ResponseEntity<?> redeemCoupon(@PathVariable String code, HttpServletRequest request) throws HttpException, CouponNotValidForCountryException, CouponAlreadyUsedException {
        logger.info("Redeeming coupon with code: {}", code);
        String ipAddress = ipAddressDeterminer.determineClientIpAddress(request);
        CouponResponseDto coupon = CouponResponseDto.fromCoupon(couponManagementFacade.redeemCoupon(code, ipAddress));
        logger.info("Coupon redeemed successfully: {}", coupon);
        return ResponseEntity.ok(Map.of("message", "Coupon " + coupon.getCode() + " redeemed successfully. " +
                "Remaining uses: " + coupon.getRemainingUses()));
    }

}

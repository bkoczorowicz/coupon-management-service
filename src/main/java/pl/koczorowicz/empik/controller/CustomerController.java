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
     * This endpoint is used to redeem a coupon by its code.
     * It checks the validity of the coupon and returns the remaining uses.
     *
     * @param code The code of the coupon to be redeemed.
     * @param request The HTTP request containing client IP address.
     * @return A response entity with a success message and remaining uses of the coupon.
     * @throws HttpException If there is an error in processing the request.
     * @throws CouponNotValidForCountryException If the coupon is not valid for the country of the user.
     * @throws CouponAlreadyUsedException If the coupon has already been used.
     */
    @PostMapping("/coupon/{code}/remaining-uses")
    public ResponseEntity<?> redeemCoupon(@PathVariable String code, @RequestParam String userName, HttpServletRequest request) throws HttpException, CouponNotValidForCountryException, CouponAlreadyUsedException {
        logger.info("Redeeming coupon with code: {}", code);
        String ipAddress = ipAddressDeterminer.determineClientIpAddress(request);
        CouponResponseDto coupon = CouponResponseDto.fromCoupon(couponManagementFacade.redeemCoupon(code, ipAddress, userName));
        logger.info("Coupon redeemed successfully: {}", coupon);
        return ResponseEntity.ok(Map.of("message", "Coupon " + coupon.getCode() + " redeemed successfully. " +
                "Remaining uses: " + coupon.getRemainingUses()));
    }

}

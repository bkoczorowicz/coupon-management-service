package pl.koczorowicz.empik.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.koczorowicz.empik.dto.CouponRequestDto;
import pl.koczorowicz.empik.dto.CouponResponseDto;
import pl.koczorowicz.empik.facade.CouponManagementFacade;
import pl.koczorowicz.empik.model.Coupon;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CouponManagementFacade couponManagementFacade;

    /**
     * This endpoint is used to create a new coupon.
     * It accepts a JSON object representing the coupon details.
     * The coupon is created and saved in the database.
     *
     * @param couponDto The DTO containing coupon details.
     * @return A response entity with a success message and the created coupon data.
     */
    @PostMapping(path ="/coupon", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createCoupon(@RequestBody CouponRequestDto couponDto) {
        Coupon savedCoupon = couponManagementFacade.createNewCoupon(CouponRequestDto.toNewCoupon(couponDto));
        return ResponseEntity.ok(Map.of(
                "message", "Coupon created successfully",
                "data", CouponResponseDto.fromCoupon(savedCoupon)
        ));
    }

    /**
     * This endpoint is used to delete an existing coupon.
     *
     * @param code The code of the coupon to be updated.
     * @return A response entity with a success message that the coupon has been deleted.
     */
    @DeleteMapping(path ="/coupon/{code}", produces = "application/json")
    public ResponseEntity<?> deleteCoupon(@PathVariable String code) {
        couponManagementFacade.deleteCoupon(code);
        return ResponseEntity.ok(Map.of("message", "Coupon deleted successfully"));
    }

    /**
     * This endpoint is used to get details about an existing coupon.
     *
     * @param code The code of the coupon to be viewed.
     * @return A response entity with a success message and the requested coupon data.
     */
    @GetMapping(path ="/coupon/{code}", produces = "application/json")
    public ResponseEntity<?> getCoupon(@PathVariable String code) {
        Coupon coupon = couponManagementFacade.getCouponByCode(code);
        return ResponseEntity.ok(Map.of(
                "data", CouponResponseDto.fromCoupon(coupon)
        ));
    }


}

package pl.koczorowicz.empik.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.koczorowicz.empik.exception.CouponNotValidForCountryException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
        /**
        * Handles CouponNotValidForCountryException and returns a 403 Forbidden response with the exception message.
        *
        * @param ex the exception to handle
        * @return ResponseEntity with status 403 and the exception message
        */
     @ExceptionHandler(CouponNotValidForCountryException.class)
     public ResponseEntity<?> handleCouponNotValidForCountryException(CouponNotValidForCountryException ex) {
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "message", ex.getMessage()));
     }




}

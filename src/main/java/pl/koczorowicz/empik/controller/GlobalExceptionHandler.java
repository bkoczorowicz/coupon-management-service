package pl.koczorowicz.empik.controller;

import org.apache.http.HttpException;
import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.koczorowicz.empik.exception.CouponAlreadyUsedException;
import pl.koczorowicz.empik.exception.CouponNotValidForCountryException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        /**
        * Handles CouponNotValidForCountryException and returns a 403 Forbidden response with the exception message.
        *
        * @param ex the exception to handle
        * @return ResponseEntity with status 403 and the exception message
        */
     @ExceptionHandler(CouponNotValidForCountryException.class)
     public ResponseEntity<?> handleCouponNotValidForCountryException(CouponNotValidForCountryException ex) {
         logger.error("Coupon not valid for country: {}", ex.getMessage());
         return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                    "message", ex.getMessage()));
     }

    /**
     * Handles OptimisticEntityLockException and returns a 409 Conflict response with the exception message.
     * @param ex the exception to handle
     * @return ResponseEntity with status 409 and the exception message
     */
    @ExceptionHandler(OptimisticEntityLockException.class)
    public ResponseEntity<?> handleOptimisticEntityLockException(OptimisticEntityLockException ex) {
        logger.error("Optimistic lock exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "message", ex.getMessage()));
    }

    /**
     * Handles CouponAlreadyUsedException and returns a 400 Bad Request response with the exception message.
     *
     * @param ex the exception to handle
     * @return ResponseEntity with status 400 and the exception message
     */
    @ExceptionHandler(CouponAlreadyUsedException.class)
    public ResponseEntity<?> handleCouponAlreadyUsedException(CouponAlreadyUsedException ex) {
        logger.error("Coupon already used: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "message", ex.getMessage()));
    }

    /**
     * Handles NoSuchElementException and returns a 404 Not Found response with the exception message.
     *
     * @param ex the exception to handle
     * @return ResponseEntity with status 404 and the exception message
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
        logger.error("Requested coupon not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "message", "Requested resource not found: " + ex.getMessage()));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<?> handleHttpException(HttpException ex) {
        logger.error("HTTP exception occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "HTTP error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("Data integrity violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                "message", "Coupon with the given name already exists",
                "details", ex.getMessage()));
    }

    /**
     * Handles all other exceptions and returns a 500 Internal Server Error response with the exception message.
     *
     * @param ex the exception to handle
     * @return ResponseEntity with status 500 and the exception message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        logger.error("An unexpected error occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "message", "An unexpected error occurred: " + ex.getMessage()));
    }

}

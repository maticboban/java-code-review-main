package schwarz.jobs.interview.coupon.exception;

public class CouponAlreadyExistsException extends RuntimeException {
    
    public CouponAlreadyExistsException(String message) {
        super(message);
    }

    public CouponAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}

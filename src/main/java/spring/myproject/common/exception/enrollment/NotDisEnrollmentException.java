package spring.myproject.common.exception.enrollment;

public class NotDisEnrollmentException extends RuntimeException {

    public NotDisEnrollmentException() {
    }

    public NotDisEnrollmentException(String message) {
        super(message);
    }

    public NotDisEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotDisEnrollmentException(Throwable cause) {
        super(cause);
    }

    public NotDisEnrollmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

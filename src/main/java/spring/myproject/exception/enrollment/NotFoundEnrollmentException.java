package spring.myproject.exception.enrollment;

public class NotFoundEnrollmentException extends RuntimeException {
    public NotFoundEnrollmentException() {
    }

    public NotFoundEnrollmentException(String message) {
        super(message);
    }

    public NotFoundEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundEnrollmentException(Throwable cause) {
        super(cause);
    }

    public NotFoundEnrollmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

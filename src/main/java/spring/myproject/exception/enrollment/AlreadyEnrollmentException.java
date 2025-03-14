package spring.myproject.exception.enrollment;

public class AlreadyEnrollmentException extends RuntimeException {

    public AlreadyEnrollmentException() {
    }

    public AlreadyEnrollmentException(String message) {
        super(message);
    }

    public AlreadyEnrollmentException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyEnrollmentException(Throwable cause) {
        super(cause);
    }

    public AlreadyEnrollmentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package spring.myproject.common.exception.attend;

public class NotFoundAttendException extends RuntimeException {

    public NotFoundAttendException() {
    }

    public NotFoundAttendException(String message) {
        super(message);
    }

    public NotFoundAttendException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAttendException(Throwable cause) {
        super(cause);
    }

    public NotFoundAttendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

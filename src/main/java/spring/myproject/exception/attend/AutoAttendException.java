package spring.myproject.exception.attend;

public class AutoAttendException extends RuntimeException {
    public AutoAttendException() {
    }

    public AutoAttendException(String message) {
        super(message);
    }

    public AutoAttendException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutoAttendException(Throwable cause) {
        super(cause);
    }

    public AutoAttendException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

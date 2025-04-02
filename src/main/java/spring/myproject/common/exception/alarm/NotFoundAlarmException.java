package spring.myproject.common.exception.alarm;

public class NotFoundAlarmException extends RuntimeException {

    public NotFoundAlarmException() {
    }

    public NotFoundAlarmException(String message) {
        super(message);
    }

    public NotFoundAlarmException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAlarmException(Throwable cause) {
        super(cause);
    }

    public NotFoundAlarmException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package spring.myproject.exception.meeting;

public class NotAuthrizeException extends RuntimeException {

    public NotAuthrizeException() {
    }

    public NotAuthrizeException(String message) {
        super(message);
    }

    public NotAuthrizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthrizeException(Throwable cause) {
        super(cause);
    }

    public NotAuthrizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

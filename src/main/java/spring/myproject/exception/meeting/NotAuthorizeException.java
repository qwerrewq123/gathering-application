package spring.myproject.exception.meeting;

public class NotAuthorizeException extends RuntimeException {

    public NotAuthorizeException() {
    }

    public NotAuthorizeException(String message) {
        super(message);
    }

    public NotAuthorizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAuthorizeException(Throwable cause) {
        super(cause);
    }

    public NotAuthorizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

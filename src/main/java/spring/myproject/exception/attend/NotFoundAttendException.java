package spring.myproject.exception.attend;

public class NotFoundAttend extends RuntimeException {

    public NotFoundAttend() {
    }

    public NotFoundAttend(String message) {
        super(message);
    }

    public NotFoundAttend(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundAttend(Throwable cause) {
        super(cause);
    }

    public NotFoundAttend(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

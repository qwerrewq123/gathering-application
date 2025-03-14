package spring.myproject.exception.user;

public class NotFoundEmailExeption extends RuntimeException {
    public NotFoundEmailExeption() {
    }

    public NotFoundEmailExeption(String message) {
        super(message);
    }

    public NotFoundEmailExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundEmailExeption(Throwable cause) {
        super(cause);
    }

    public NotFoundEmailExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

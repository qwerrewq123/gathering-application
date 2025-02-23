package spring.myproject.domain.like.exception;

public class NotFoundLikeException extends RuntimeException {

    public NotFoundLikeException() {
    }

    public NotFoundLikeException(String message) {
        super(message);
    }

    public NotFoundLikeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundLikeException(Throwable cause) {
        super(cause);
    }

    public NotFoundLikeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

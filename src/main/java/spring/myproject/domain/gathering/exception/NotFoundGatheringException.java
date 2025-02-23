package spring.myproject.domain.gathering.exception;

public class NotFoundGatheringException extends RuntimeException {

    public NotFoundGatheringException() {
    }

    public NotFoundGatheringException(String message) {
        super(message);
    }

    public NotFoundGatheringException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundGatheringException(Throwable cause) {
        super(cause);
    }

    public NotFoundGatheringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

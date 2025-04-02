package spring.myproject.common.exception.fcm;

public class NotFoundTopicException extends RuntimeException {

    public NotFoundTopicException() {
    }

    public NotFoundTopicException(String message) {
        super(message);
    }

    public NotFoundTopicException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundTopicException(Throwable cause) {
        super(cause);
    }

    public NotFoundTopicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

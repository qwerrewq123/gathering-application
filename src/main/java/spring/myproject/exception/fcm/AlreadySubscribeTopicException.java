package spring.myproject.exception.fcm;

public class AlreadySubscribeTopicException extends RuntimeException {

    public AlreadySubscribeTopicException() {
    }

    public AlreadySubscribeTopicException(String message) {
        super(message);
    }

    public AlreadySubscribeTopicException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadySubscribeTopicException(Throwable cause) {
        super(cause);
    }

    public AlreadySubscribeTopicException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

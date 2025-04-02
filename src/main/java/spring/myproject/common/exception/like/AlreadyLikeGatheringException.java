package spring.myproject.common.exception.like;

public class AlreadyLikeGatheringException extends RuntimeException {
    public AlreadyLikeGatheringException() {
    }

    public AlreadyLikeGatheringException(String message) {
        super(message);
    }

    public AlreadyLikeGatheringException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyLikeGatheringException(Throwable cause) {
        super(cause);
    }

    public AlreadyLikeGatheringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

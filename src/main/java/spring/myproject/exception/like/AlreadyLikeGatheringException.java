package spring.myproject.exception.like;

public class AlreadyLikeGathering extends RuntimeException {
    public AlreadyLikeGathering() {
    }

    public AlreadyLikeGathering(String message) {
        super(message);
    }

    public AlreadyLikeGathering(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyLikeGathering(Throwable cause) {
        super(cause);
    }

    public AlreadyLikeGathering(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

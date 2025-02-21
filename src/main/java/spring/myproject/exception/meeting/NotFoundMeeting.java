package spring.myproject.exception.meeting;

public class NotFoundMeeting extends RuntimeException {
    public NotFoundMeeting() {
    }

    public NotFoundMeeting(String message) {
        super(message);
    }

    public NotFoundMeeting(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundMeeting(Throwable cause) {
        super(cause);
    }

    public NotFoundMeeting(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

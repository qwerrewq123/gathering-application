package spring.myproject.domain.meeting.exception;

public class NotFoundMeetingExeption extends RuntimeException {
    public NotFoundMeetingExeption() {
    }

    public NotFoundMeetingExeption(String message) {
        super(message);
    }

    public NotFoundMeetingExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundMeetingExeption(Throwable cause) {
        super(cause);
    }

    public NotFoundMeetingExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

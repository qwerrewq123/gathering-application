package spring.myproject.domain.meeting.exception;

public class MeetingIsnotEmptyException extends RuntimeException {
    public MeetingIsnotEmptyException() {
    }

    public MeetingIsnotEmptyException(String message) {
        super(message);
    }

    public MeetingIsnotEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetingIsnotEmptyException(Throwable cause) {
        super(cause);
    }

    public MeetingIsnotEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

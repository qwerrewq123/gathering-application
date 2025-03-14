package spring.myproject.exception.meeting;

public class MeetingIsNotEmptyException extends RuntimeException {
    public MeetingIsNotEmptyException() {
    }

    public MeetingIsNotEmptyException(String message) {
        super(message);
    }

    public MeetingIsNotEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MeetingIsNotEmptyException(Throwable cause) {
        super(cause);
    }

    public MeetingIsNotEmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

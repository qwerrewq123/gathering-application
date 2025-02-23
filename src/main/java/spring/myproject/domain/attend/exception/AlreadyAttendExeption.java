package spring.myproject.domain.attend.exception;

public class AlreadyAttendExeption extends RuntimeException {
    public AlreadyAttendExeption() {
    }

    public AlreadyAttendExeption(String message) {
        super(message);
    }

    public AlreadyAttendExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public AlreadyAttendExeption(Throwable cause) {
        super(cause);
    }

    public AlreadyAttendExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

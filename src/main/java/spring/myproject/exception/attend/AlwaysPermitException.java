package spring.myproject.exception.attend;

public class AlwaysPermitException extends RuntimeException {

    public AlwaysPermitException() {
    }

    public AlwaysPermitException(String message) {
        super(message);
    }

    public AlwaysPermitException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlwaysPermitException(Throwable cause) {
        super(cause);
    }

    public AlwaysPermitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

package spring.myproject.domain.attend.exception;

public class NotWithdrawException extends RuntimeException {
    public NotWithdrawException() {
    }

    public NotWithdrawException(String message) {
        super(message);
    }

    public NotWithdrawException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotWithdrawException(Throwable cause) {
        super(cause);
    }

    public NotWithdrawException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

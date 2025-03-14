package spring.myproject.exception.user;

public class DuplicateEmailExeption extends RuntimeException {
    public DuplicateEmailExeption() {
    }

    public DuplicateEmailExeption(String message) {
        super(message);
    }

    public DuplicateEmailExeption(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateEmailExeption(Throwable cause) {
        super(cause);
    }

    public DuplicateEmailExeption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

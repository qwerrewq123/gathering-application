package spring.myproject.exception.user;

public class UnCorrectCertification extends RuntimeException {

    public UnCorrectCertification() {
    }

    public UnCorrectCertification(String message) {
        super(message);
    }

    public UnCorrectCertification(String message, Throwable cause) {
        super(message, cause);
    }

    public UnCorrectCertification(Throwable cause) {
        super(cause);
    }

    public UnCorrectCertification(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

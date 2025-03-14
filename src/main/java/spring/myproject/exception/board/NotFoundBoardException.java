package spring.myproject.exception.board;

public class NotFoundBoardException extends RuntimeException {
    public NotFoundBoardException(String message) {
        super(message);
    }

    public NotFoundBoardException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundBoardException(Throwable cause) {
        super(cause);
    }

    public NotFoundBoardException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

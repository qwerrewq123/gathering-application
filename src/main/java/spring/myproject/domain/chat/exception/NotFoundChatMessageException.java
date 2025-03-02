package spring.myproject.domain.chat.exception;

public class NotFoundChatMessageException extends RuntimeException{
    public NotFoundChatMessageException() {
    }

    public NotFoundChatMessageException(String message) {
        super(message);
    }

    public NotFoundChatMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundChatMessageException(Throwable cause) {
        super(cause);
    }

    public NotFoundChatMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

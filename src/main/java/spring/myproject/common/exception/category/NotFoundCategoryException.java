package spring.myproject.common.exception.category;

public class NotFoundCategoryException  extends RuntimeException{


    public NotFoundCategoryException(String message) {
        super(message);
    }

    public NotFoundCategoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundCategoryException(Throwable cause) {
        super(cause);
    }

    public NotFoundCategoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

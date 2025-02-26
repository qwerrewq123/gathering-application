package spring.myproject.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import spring.myproject.domain.error.dto.response.ErrorResponse;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.util.AbstractErrorResponse;

import static spring.myproject.util.ConstClass.*;

@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(MethodNotAllowedException.class)
    ResponseEntity<ErrorResponse> handleNotFoundUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(METHOD_NOT_ALLOWED_CODE, METHOD_NOT_ALLOWED_MESSAGE)
                , HttpStatus.FORBIDDEN);
    }
}

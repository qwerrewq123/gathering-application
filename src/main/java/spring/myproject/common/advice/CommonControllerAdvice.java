package spring.myproject.common.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;
@Order(2)
@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(MethodNotAllowedException.class)
    ResponseEntity<ErrorResponse> handleNotFoundUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(METHOD_NOT_ALLOWED_CODE, METHOD_NOT_ALLOWED_MESSAGE)
                , HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(DB_ERROR_CODE, DB_ERROR_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
}

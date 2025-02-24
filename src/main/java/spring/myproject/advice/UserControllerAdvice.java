package spring.myproject.advice;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.error.dto.response.ErrorResponse;
import spring.myproject.domain.user.controller.UserController;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.domain.user.exception.UnCorrectPasswordException;
import spring.myproject.util.AbstractErrorResponse;

import static spring.myproject.util.ConstClass.*;

@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerAdvice {
    @ExceptionHandler(NotFoundUserException.class)
    ResponseEntity<ErrorResponse> handleNotFoundUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_USER_CODE, NOT_FOUND_USER_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnCorrectPasswordException.class)
    ResponseEntity<ErrorResponse> handleUnCorrectPasswordException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(UNCORRECT_CODE, UNCORRECT_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MessagingException.class)
    ResponseEntity<ErrorResponse> handleMessagingException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(FAIL_MESSAGE_CODE, FAIL_MESSAGE_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(DB_ERROR_CODE, DB_ERROR_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
}

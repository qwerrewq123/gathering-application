package spring.myproject.common.advice;

import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.common.dto.response.ErrorResponse;
import spring.myproject.controller.user.UserController;
import spring.myproject.exception.user.*;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;

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
                AbstractErrorResponse.getErrorResponse(UN_CORRECT_CODE, UN_CORRECT_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MessagingException.class)
    ResponseEntity<ErrorResponse> handleMessagingException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(FAIL_MESSAGE_CODE, FAIL_MESSAGE_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ExistUserException.class)
    ResponseEntity<ErrorResponse> handleExistUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(EXIST_CODE, EXIST_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundEmailExeption.class)
    ResponseEntity<ErrorResponse> handleNotFoundEmailException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_EMAIL_CODE, NOT_EMAIL_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DuplicateEmailExeption.class)
    ResponseEntity<ErrorResponse> handleDuplicateEmailException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(DUPLICATE_EMAIL_CODE, DUPLICATE_EMAIL_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(DB_ERROR_CODE, DB_ERROR_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
}

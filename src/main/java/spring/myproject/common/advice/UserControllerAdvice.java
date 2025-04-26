package spring.myproject.common.advice;

import jakarta.mail.MessagingException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.user.*;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.controller.user.UserController;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;
@Order(1)
@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerAdvice {
    @ExceptionHandler(ExistUserException.class)
    ResponseEntity<ErrorResponse> handleExistUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(EXIST_CODE, EXIST_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundUserException.class)
    ResponseEntity<ErrorResponse> handleNotFoundUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_USER_CODE, NOT_FOUND_USER_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotAuthorizeException.class)
    ResponseEntity<ErrorResponse> handleNotAuthorizeException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_AUTHORIZE_CODE, NOT_AUTHORIZED_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnCorrectPasswordException.class)
    ResponseEntity<ErrorResponse> handleUnCorrectPasswordException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(UN_CORRECT_PASSWORD_CODE, UN_CORRECT_PASSWORD_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MessagingException.class)
    ResponseEntity<ErrorResponse> handleMessagingException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(FAIL_MESSAGE_CODE, FAIL_MESSAGE_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DuplicateEmailExeption.class)
    ResponseEntity<ErrorResponse> handleDuplicateEmailException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(DUPLICATE_EMAIL_CODE, DUPLICATE_EMAIL_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundCertificationException.class)
    ResponseEntity<ErrorResponse> handleNotFoundCertificationException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_CERTIFICATION_CODE, NOT_FOUND_CERTIFICATION_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnCorrectCertification.class)
    ResponseEntity<ErrorResponse> handleUnCorrectCertification(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(UN_CORRECT_CERTIFICATION_CODE, UN_CORRECT_CERTIFICATION_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }


}

package spring.myproject.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.enrollment.controller.EnrollmentController;
import spring.myproject.domain.enrollment.exception.AlreadyEnrollmentException;
import spring.myproject.domain.enrollment.exception.NotFoundEnrollmentException;
import spring.myproject.domain.error.dto.response.ErrorResponse;
import spring.myproject.domain.gathering.exception.NotFoundGatheringException;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.util.AbstractErrorResponse;

import static spring.myproject.util.ConstClass.*;

@RestControllerAdvice(basePackageClasses = EnrollmentController.class)
public class EnrollmentControllerAdvice {
    @ExceptionHandler(NotFoundUserException.class)
    ResponseEntity<ErrorResponse> handleNotFoundUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_USER_CODE, NOT_FOUND_USER_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundGatheringException.class)
    ResponseEntity<ErrorResponse> handleNotFoundGatheringException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_GATHERING_CODE, NOT_FOUND_GATHERING_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AlreadyEnrollmentException.class)
    ResponseEntity<ErrorResponse> handleAlreadyEnrollmentException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(ALREADY_ENROLLMENT_CODE, ALREADY_ENROLLMENT_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundEnrollmentException.class)
    ResponseEntity<ErrorResponse> handleNotFoundEnrollmentException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_ENROLLMENT_CODE, NOT_FOUND_ENROLLMENT_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(DB_ERROR_CODE, DB_ERROR_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
}

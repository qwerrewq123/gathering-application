package spring.myproject.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.controller.enrollment.EnrollmentController;
import spring.myproject.exception.enrollment.AlreadyEnrollmentException;
import spring.myproject.exception.enrollment.NotFoundEnrollmentException;
import spring.myproject.common.dto.response.ErrorResponse;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;

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

package spring.myproject.common.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.common.exception.enrollment.NotDisEnrollmentException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.controller.enrollment.EnrollmentController;
import spring.myproject.common.exception.enrollment.AlreadyEnrollmentException;
import spring.myproject.common.exception.enrollment.NotFoundEnrollmentException;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;
@Order(1)
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
    @ExceptionHandler(NotDisEnrollmentException.class)
    ResponseEntity<ErrorResponse> handleNotDisEnrollmentException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_DIS_ENROLLMENT_CODE, NOT_DIS_ENROLLMENT_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotAuthorizeException.class)
    ResponseEntity<ErrorResponse> handleNotAuthorizeException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_AUTHORIZE_CODE, NOT_AUTHORIZED_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }

}

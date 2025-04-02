package spring.myproject.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.controller.attend.AttendController;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.exception.attend.*;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;
import static spring.myproject.utils.ConstClass.DB_ERROR_MESSAGE;
@Order(1)
@Slf4j
@RestControllerAdvice(basePackageClasses = AttendController.class)
public class AttendControllerAdvice {

    @ExceptionHandler(NotFoundUserException.class)
    ResponseEntity<ErrorResponse> handleNotFoundUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_USER_CODE, NOT_FOUND_USER_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundMeetingExeption.class)
    ResponseEntity<ErrorResponse> handleNotFoundMeetingException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_MEETING_CODE, NOT_FOUND_ENROLLMENT_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AutoAttendException.class)
    ResponseEntity<ErrorResponse> handleAutoAttendException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(AUTO_ATTEND_CODE, AUTO_ATTEND_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AlreadyAttendExeption.class)
    ResponseEntity<ErrorResponse> handleAlreadyAttendException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(ALREADY_ATTEND_CODE, ALREADY_ATTEND_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotWithdrawException.class)
    ResponseEntity<ErrorResponse> handleNotWithdrawException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_WITHDRAW_CODE, NOT_WITHDRAW_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundAttendException.class)
    ResponseEntity<ErrorResponse> handleNotFoundAttendException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_ATTEND_CODE, NOT_FOUND_ATTEND_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(AlwaysPermitException.class)
    ResponseEntity<ErrorResponse> handleAlwaysPermitException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(ALWAYS_PERMIT_CODE, ALWAYS_PERMIT_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }

}

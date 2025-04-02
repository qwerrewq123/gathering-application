package spring.myproject.common.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.controller.alarm.AlarmController;
import spring.myproject.common.exception.alarm.NotFoundAlarmException;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;
@Order(1)
@RestControllerAdvice(basePackageClasses = AlarmController.class)
public class AlarmControllerAdvice {

    @ExceptionHandler(NotFoundUserException.class)
    ResponseEntity<ErrorResponse> handleNotFoundUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_USER_CODE, NOT_FOUND_USER_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundAlarmException.class)
    ResponseEntity<ErrorResponse> handleNotFoundAlarmException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_ALARM_CODE, NOT_FOUND_ALARM_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }

}

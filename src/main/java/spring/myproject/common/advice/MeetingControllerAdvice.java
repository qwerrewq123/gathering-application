package spring.myproject.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.controller.meeting.MeetingController;
import spring.myproject.common.exception.meeting.MeetingIsNotEmptyException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.meeting.NotFoundMeetingExeption;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;
@Slf4j
@Order(1)
@RestControllerAdvice(basePackageClasses = MeetingController.class)
public class MeetingControllerAdvice {
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
    @ExceptionHandler(NotFoundMeetingExeption.class)
    ResponseEntity<ErrorResponse> handleNotFoundMeetingException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_MEETING_CODE, NOT_FOUND_MEETING_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotAuthorizeException.class)
    ResponseEntity<ErrorResponse> handleNotAuthorizeException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_AUTHORIZE_CODE, NOT_AUTHORIZED_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MeetingIsNotEmptyException.class)
    ResponseEntity<ErrorResponse> handleMeetingIsNotEmptyException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(MEETING_IS_NOT_EMPTY_CODE, MEETING_IS_NOT_EMPTY_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }

}

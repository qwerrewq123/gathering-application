package spring.myproject.common.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.common.exception.alarm.NotFoundAlarmException;
import spring.myproject.common.exception.board.NotFoundBoardException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.controller.alarm.AlarmController;
import spring.myproject.controller.board.BoardController;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;

@Order(1)
@RestControllerAdvice(basePackageClasses = BoardController.class)
public class BoardControllerAdvice {

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
    @ExceptionHandler(NotAuthorizeException.class)
    ResponseEntity<ErrorResponse> handleNotAuthorizeException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_AUTHORIZE_CODE, NOT_AUTHORIZED_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundBoardException.class)
    ResponseEntity<ErrorResponse> handleNotFoundBoardException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_BOARD_CODE, NOT_FOUND_BOARD_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }



}

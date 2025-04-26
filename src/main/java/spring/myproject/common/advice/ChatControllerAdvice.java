package spring.myproject.common.advice;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.common.exception.alarm.NotFoundAlarmException;
import spring.myproject.common.exception.chat.NotFoundChatParticipantException;
import spring.myproject.common.exception.chat.NotFoundChatRoomException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.controller.alarm.AlarmController;
import spring.myproject.controller.chat.ChatController;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;

@Order(1)
@RestControllerAdvice(basePackageClasses = ChatController.class)
public class ChatControllerAdvice {

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
    @ExceptionHandler(NotFoundChatRoomException.class)
    ResponseEntity<ErrorResponse> handleNotFoundChatRoomException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_CHAT_ROOM_CODE, NOT_FOUND_CHAT_ROOM_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundChatParticipantException.class)
    ResponseEntity<ErrorResponse> handleNotFoundChatParticipantException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_CHAT_PARTICIPANT_CODE, NOT_FOUND_CHAT_PARTICIPANT_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }



}

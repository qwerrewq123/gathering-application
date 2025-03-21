package spring.myproject.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.common.dto.response.ErrorResponse;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.controller.like.LikeController;
import spring.myproject.exception.like.AlreadyLikeGathering;
import spring.myproject.exception.like.NotFoundLikeException;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.utils.AbstractErrorResponse;

import static spring.myproject.utils.ConstClass.*;

@RestControllerAdvice(basePackageClasses = LikeController.class)
public class LikeControllerAdvice {
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
    @ExceptionHandler(AlreadyLikeGathering.class)
    ResponseEntity<ErrorResponse> handleAlreadyLikeException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(ALREADY_LIKE_CODE, ALREADY_LIKE_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundLikeException.class)
    ResponseEntity<ErrorResponse> handleNotLikeException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_LIKE_CODE, NOT_FOUND_LIKE_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(DB_ERROR_CODE, DB_ERROR_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
}

package spring.myproject.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.category.exception.NotFoundCategoryException;
import spring.myproject.common.dto.response.ErrorResponse;
import spring.myproject.domain.gathering.controller.GatheringController;
import spring.myproject.domain.gathering.exception.NotFoundGatheringException;
import spring.myproject.domain.meeting.exception.NotAuthorizeException;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.util.AbstractErrorResponse;

import java.io.IOException;

import static spring.myproject.util.ConstClass.*;

@RestControllerAdvice(basePackageClasses = GatheringController.class)
public class GatheringControllerAdvice {
    @ExceptionHandler(NotFoundUserException.class)
    ResponseEntity<ErrorResponse> handleNotFoundUserException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_USER_CODE, NOT_FOUND_USER_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NotFoundCategoryException.class)
    ResponseEntity<ErrorResponse> handleNotFoundCategoryException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_CATEGORY_CODE, NOT_FOUND_CATEGORY_MESSAGE)
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
    @ExceptionHandler(IOException.class)
    ResponseEntity<ErrorResponse> handleIOException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(UPLOAD_FAIL_CODE, UPLOAD_FAIL_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handleException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(DB_ERROR_CODE, DB_ERROR_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }
}

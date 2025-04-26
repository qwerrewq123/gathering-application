package spring.myproject.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.common.exception.category.NotFoundCategoryException;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.image.NotFoundImageException;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.controller.gathering.GatheringController;
import spring.myproject.controller.image.ImageController;
import spring.myproject.dto.response.ErrorResponse;
import spring.myproject.utils.AbstractErrorResponse;

import java.io.IOException;

import static spring.myproject.utils.ConstClass.*;

@Slf4j
@Order(1)
@RestControllerAdvice(basePackageClasses = ImageController.class)
public class ImageControllerAdvice {
    @ExceptionHandler(NotFoundImageException.class)
    ResponseEntity<ErrorResponse> handleNotFoundImageException(){
        return new ResponseEntity<>(
                AbstractErrorResponse.getErrorResponse(NOT_FOUND_IMAGE_CODE, NOT_FOUND_IMAGE_MESSAGE)
                , HttpStatus.BAD_REQUEST);
    }



}

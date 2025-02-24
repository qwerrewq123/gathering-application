package spring.myproject.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.like.controller.LikeController;

@RestControllerAdvice(basePackageClasses = LikeController.class)
public class LikeControllerAdvice {
}

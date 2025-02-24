package spring.myproject.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.recommend.controller.RecommendController;

@RestControllerAdvice(basePackageClasses = RecommendController.class)
public class RecommendControllerAdvice {
}

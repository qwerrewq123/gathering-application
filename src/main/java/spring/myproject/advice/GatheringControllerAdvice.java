package spring.myproject.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.gathering.controller.GatheringController;

@RestControllerAdvice(basePackageClasses = GatheringController.class)
public class GatheringControllerAdvice {
}

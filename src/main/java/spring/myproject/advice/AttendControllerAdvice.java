package spring.myproject.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.attend.controller.AttendController;

@RestControllerAdvice(basePackageClasses = AttendController.class)
public class AttendControllerAdvice {
}

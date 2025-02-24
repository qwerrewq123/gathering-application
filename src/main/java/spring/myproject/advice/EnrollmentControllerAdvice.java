package spring.myproject.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.enrollment.controller.EnrollmentController;

@RestControllerAdvice(basePackageClasses = EnrollmentController.class)
public class EnrollmentControllerAdvice {
}

package spring.myproject.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.user.controller.UserController;

@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserControllerAdvice {
}

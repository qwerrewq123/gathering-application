package spring.myproject.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.alarm.controller.AlarmController;

@RestControllerAdvice(basePackageClasses = AlarmController.class)
public class AlarmControllerAdvice {
}

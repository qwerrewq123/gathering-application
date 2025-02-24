package spring.myproject.advice;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.myproject.domain.meeting.controller.MeetingController;

@RestControllerAdvice(basePackageClasses = MeetingController.class)
public class MeetingControllerAdvice {
}

package spring.myproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    //load balancer health check
    @GetMapping("/health")
    public String health(){
        return "health";
    }
}

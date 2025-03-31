package spring.myproject.controller.gathering;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import spring.myproject.controller.user.UserController;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
public class GatheringControllerTest {
    @Autowired
    MockMvc mockMvc;
}

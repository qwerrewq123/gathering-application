package spring.myproject.domain.fail.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.fail.Fail;
import spring.myproject.domain.fail.repostory.FailRepository;
import spring.myproject.domain.user.User;

@Service
@RequiredArgsConstructor
@Transactional
public class FailService {

    private final FailRepository failRepository;

    public void insertFail(User user) {
        String message = "email send Fail";
        failRepository.save(Fail.of(user, message));
    }
}

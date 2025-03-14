package spring.myproject.async;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import spring.myproject.service.fail.FailService;
import spring.myproject.dto.request.user.EmailCertificationRequest;
import spring.myproject.provider.EmailProvider;

@Service
@RequiredArgsConstructor
public class AsyncService {

    private final EmailProvider emailProvider;
    private final FailService failService;

    @Async("customAsyncExecutor")
    public void asyncTask(EmailCertificationRequest emailCertificationRequest){
        try {
            emailCertification(emailCertificationRequest);
        }catch (MessagingException e){
            failService.send(emailCertificationRequest.getClientId());
        }
    }

    private void emailCertification(EmailCertificationRequest emailCertificationRequest) throws MessagingException {
        emailProvider.sendCertificationMail(emailCertificationRequest.getEmail(), certificationNumber());
    }

    private String certificationNumber(){
        int number = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(number);
    }
}

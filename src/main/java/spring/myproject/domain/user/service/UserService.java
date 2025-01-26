package spring.myproject.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.dto.request.user.EmailCertificationRequest;
import spring.myproject.dto.request.user.IdCheckRequest;
import spring.myproject.dto.request.user.SignInRequest;
import spring.myproject.dto.request.user.UserRequest;
import spring.myproject.provider.EmailProvider;
import spring.myproject.provider.JwtProvider;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailProvider emailProvider;

    @Value("${file.dir}")
    private String fileDir;

    public Boolean idCheck(IdCheckRequest idCheckRequest) {

        Boolean exist = userRepository.existsByUsername(idCheckRequest.getUsername());
        return exist;
    }

    public void signUp(UserRequest userRequest, MultipartFile file) throws IOException {


        Image image = null;
        if(!file.isEmpty()){
            String fullPath = fileDir + file.getOriginalFilename();
            file.transferTo(new File(fullPath));
            image = Image.builder()
                    .url(fullPath)
                    .build();
            imageRepository.save(image);

        }

        User user = User.builder()
                .age(userRequest.getAge())
                .email(userRequest.getEmail())
                .hobby(userRequest.getHobby())
                .address(userRequest.getAddress())
                .username(userRequest.getUsername())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .profileImage(image)
                .build();

        userRepository.save(user);


    }

    public String signIn(SignInRequest signInRequest) {
        User user = userRepository.findByUsername(signInRequest.getUsername());
        if(user == null){
            throw new IllegalArgumentException("회원이 존재하지 않습니다");
        }
        boolean matches = passwordEncoder.matches(signInRequest.getPassword(), user.getPassword());
        if(!matches){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return jwtProvider.create(user.getUsername());
    }

    public void emailCertification(EmailCertificationRequest emailCertificationRequest) {

        emailProvider.sendCertificationMail(emailCertificationRequest.getEmail(), certificationNumber());

    }

    private String certificationNumber(){
        int number = (int) (Math.random() * 9000) + 1000;
        return String.valueOf(number);
    }
}

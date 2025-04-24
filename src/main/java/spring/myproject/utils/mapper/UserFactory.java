package spring.myproject.utils.mapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.myproject.dto.request.user.UserRequestDto;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;

import static spring.myproject.dto.request.user.UserRequestDto.*;

public class UserFactory {
    public static User toUser(SignUpRequest signUpRequest, Image image, PasswordEncoder passwordEncoder){
        return  User.builder()
                    .age(signUpRequest.getAge())
                    .email(signUpRequest.getEmail())
                    .hobby(signUpRequest.getHobby())
                    .address(signUpRequest.getAddress())
                    .username(signUpRequest.getUsername())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .role(Role.USER)
                    .profileImage(image)
                    .nickname(signUpRequest.getNickname())
                    .build();
    }

}

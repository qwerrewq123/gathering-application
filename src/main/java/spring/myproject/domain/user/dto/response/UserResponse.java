package spring.myproject.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import spring.myproject.domain.user.User;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private String username;
    private String password;
    private String email;
    private String address;
    private Integer age;
    private String hobby;
    private String image;

    public static UserResponse of(User user){
        return UserResponse.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .address(user.getAddress())
                .age(user.getAge())
                .hobby(user.getHobby())
                .image(user.getProfileImage().getUrl())
                .build();
    }
}

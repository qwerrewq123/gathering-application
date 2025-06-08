package spring.myproject.entity.user;

import ch.qos.logback.core.subst.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import spring.myproject.dto.request.user.UserRequestDto;
import spring.myproject.entity.fcm.FCMToken;
import spring.myproject.entity.image.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static spring.myproject.dto.request.user.UserRequestDto.*;

@Getter
@NoArgsConstructor
@Table(name="user")
@Builder
@AllArgsConstructor
@Entity
@EqualsAndHashCode
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String address;
    private Integer age;
    private String hobby;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String nickname;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image profileImage;
    private String refreshToken;
    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<FCMToken> tokens = new ArrayList<>();

    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public void changeProfileImage(Image profileImage){
        this.profileImage = profileImage;
    }

    public void change(UpdateRequest updateRequest, PasswordEncoder passwordEncoder){
        this.email = updateRequest.getEmail();
        this.address = updateRequest.getAddress();
        this.age = updateRequest.getAge();
        this.hobby = updateRequest.getHobby();
        this.nickname = updateRequest.getNickname();
        if(StringUtils.hasText(updateRequest.getPassword())){
            this.password = passwordEncoder.encode(updateRequest.getPassword());
        }
    }

}

package spring.myproject.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Table(name="user")
@Builder
@AllArgsConstructor
@Entity
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image profileImage;
    private String refreshToken;

    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

}

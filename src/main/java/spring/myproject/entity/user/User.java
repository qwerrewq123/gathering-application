package spring.myproject.entity.user;

import ch.qos.logback.core.subst.Token;
import jakarta.persistence.*;
import lombok.*;
import spring.myproject.entity.fcm.FCMToken;
import spring.myproject.entity.image.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "user")
    private List<FCMToken> tokens = new ArrayList<>();

    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

}

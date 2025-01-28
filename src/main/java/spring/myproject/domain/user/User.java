package spring.myproject.domain.user;

import jakarta.persistence.*;
import lombok.*;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.image.Image;

@Getter
@NoArgsConstructor
@Table(name="user")
@Builder
@AllArgsConstructor
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String address;
    private Integer age;
    private String hobby;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private Image profileImage;



}

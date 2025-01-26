package spring.myproject.domain.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.domain.user.User;

import java.util.List;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn
    private User createdBy;
    @OneToMany
    private List<User> enteredBy;
}

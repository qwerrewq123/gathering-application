package spring.myproject.domain.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.domain.user.User;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @ManyToOne
    @JoinColumn(name = "chat_room")
    private ChatRoom chatRoom;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}

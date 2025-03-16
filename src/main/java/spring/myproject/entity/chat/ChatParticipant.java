package spring.myproject.entity.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.user.User;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "chat_participant")
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    ChatRoom chatRoom;
    boolean status;

    public void changeStatus(boolean status) {
        this.status = status;
    }

    public static ChatParticipant of(ChatRoom chatRoom,User user){
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .status(false)
                .build();
    }


}

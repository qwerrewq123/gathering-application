package spring.myproject.entity.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.dto.request.chat.ChatMessageRequest;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "chat_message")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_participant_id")
    ChatParticipant chatParticipant;

    public static ChatMessage of(ChatRoom chatRoom, ChatParticipant chatParticipant, ChatMessageRequest chatMessageRequest){
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .chatParticipant(chatParticipant)
                .content(chatMessageRequest.getContent())
                .build();
    }

}

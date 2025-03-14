package spring.myproject.common.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import spring.myproject.service.chat.ChatService;
import spring.myproject.exception.meeting.NotAuthorizeException;

@Component
public class StompHandler implements ChannelInterceptor{
    @Value("${jwt.secretKey}")
    private String secretKey;
    private final ChatService chatService;

    public StompHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }

        if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            String roomId = accessor.getDestination().split("/")[2];

            if(!chatService.isRoomParticipant(username, Long.parseLong(roomId))){
                throw new NotAuthorizeException("No Authority");
            }
        }

        return message;
    }
}

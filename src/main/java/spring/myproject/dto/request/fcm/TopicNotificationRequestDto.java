package spring.myproject.dto.request.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicNotificationRequestDto {
	private String title;
	private String content;
	private String url;
	private String img;
	private String topic;
}

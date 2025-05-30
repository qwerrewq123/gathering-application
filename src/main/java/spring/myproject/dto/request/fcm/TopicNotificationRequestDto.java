package spring.myproject.dto.request.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.myproject.entity.fcm.Topic;

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

	public static TopicNotificationRequestDto from(String title, String content, Topic topic){
		   return TopicNotificationRequestDto.builder()
				.topic(topic.getTopicName())
				.title(title)
				.content(content)
				.url(null)
				.img(null)
				.build();
	}
}

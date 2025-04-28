package spring.myproject.dto.request.fcm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenNotificationRequestDto {
	private String title;
	private String content;
	private String url;
	private String img;

	public static TokenNotificationRequestDto from(String title,String content){
		return TokenNotificationRequestDto.builder()
				.title(title)
				.content(content)
				.url(null)
				.img(null)
				.build();
	}
}

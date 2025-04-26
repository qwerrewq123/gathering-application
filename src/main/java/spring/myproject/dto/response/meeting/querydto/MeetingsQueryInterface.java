package spring.myproject.dto.response.meeting.querydto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

public interface MeetingsQueryInterface {

    Long getId();
    String getTitle();
    String getCreatedBy();
    String getCreatedByNickname();
    LocalDateTime getMeetingDate();
    LocalDateTime getEndDate();
    String getContent();
    int getCount();
    String getUrl();
    Long getParticipatedId();
    String getParticipatedImageUrl();
}

package spring.myproject.domain.gathering.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


public interface EntireGatheringsQuery {

    Long getId();
    String getTitle();
    String getContent();
    LocalDateTime getRegisterDate();
    String getCategory();
    String getCreatedBy();
    String getUrl();
    int getCount();
}

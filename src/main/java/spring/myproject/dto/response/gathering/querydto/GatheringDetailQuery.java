package spring.myproject.dto.response.gathering.querydto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GatheringDetailQuery {

//    Long getId();
//    String getTitle();
//    String getContent();
//    LocalDateTime getRegisterDate();
//    String getCategory();
//    String getCreatedBy();
//    String getCreatedByUrl();
//    String getParticipatedBy();
//    String getParticipatedByNickname();
//    String getParticipatedByUrl();
//    String getUrl();
//    int getCount();
//
    private Long id;
    private String title;
    private String content;
    private LocalDateTime registerDate;
    private String category;
    private String createdBy;
    private String createdByUrl;
    private String participatedBy;
    private String participatedByNickname;
    private String participatedByUrl;
    private String url;
    private int count;
}

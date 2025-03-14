package spring.myproject.dto.response.gathering;

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

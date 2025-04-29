package spring.myproject.utils;

import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;

import java.util.UUID;


public class TopicGenerator {
    public static Topic generateTopic(Gathering gathering){
        return Topic.builder()
                .topicName(generateTopicName(gathering))
                .gathering(gathering)
                .build();
    }
    public static String generateTopicName(Gathering gathering){
        return "gathering_"+gathering.getId();
    }
}

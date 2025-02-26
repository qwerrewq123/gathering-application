package spring.myproject.util;

import spring.myproject.domain.alarm.Alarm;
import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.enrollment.Enrollment;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.GatheringCount;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.like.Like;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.recommend.Recommend;
import spring.myproject.domain.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class DummyData {

    public static Image returnDummyImage(int i){
        return Image.builder()
                .url(String.format("image%dUrl",i))
                .build();
    }
    public static User returnDummyUser(int i, Image image){
        return User.builder()
                .username(String.format("user%d",i))
                .email(String.format("user%d@gmail.com",i))
                .hobby(String.format("hobby%d",i))
                .age(i)
                .address(String.format("address%d",i))
                .password("password")
                .email(String.format("email%d",i))
                .profileImage(image)
                .build();
    }
    public static Gathering returnDummyGathering(int i, Category category, User createdBy, Image gatheringImage,
                                                 GatheringCount gatheringCount){
        return Gathering.builder()
                .title(String.format("title%d",i))
                .content(String.format("content%d",i))
                .registerDate(LocalDateTime.now())
                .category(category)
                .createBy(createdBy)
                .gatheringImage(gatheringImage)
                .gatheringCount(gatheringCount)
                .enrollments(null)
                .build();
    }
    public static Enrollment returnDummyEnrollment(User enrolledBy){
        return Enrollment.builder()
                .date(LocalDateTime.now())
                .accepted(true)
                .gathering(null)
                .enrolledBy(enrolledBy)
                .build();
    }
    public static Category returnDummyCategory(int i){
        return Category.builder()
                .name(String.format("category%d",i))
                .build();
    }
    public static GatheringCount returnDummyGatheringCount(){
        return GatheringCount.builder()
                .count(1)
                .build();
    }
    public static Meeting returnDummyMeeting(int i, User createdBy, Gathering gathering){
        return Meeting.builder()
                .boardDate(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .title(String.format("title%d",i))
                .content(String.format("content%d",i))
                .createdBy(createdBy)
                .attends(null)
                .gathering(gathering)
                .build();
    }
    public static Attend returnDummyAttend(User attendBy){
        return Attend.builder()
                .accepted(false)
                .meeting(null)
                .attendBy(attendBy)
                .date(LocalDateTime.now())
                .build();
    }
    public static Alarm returnDummyAlarm(int i, User user){
        return Alarm.builder()
                .user(user)
                .checked(false)
                .content(String.format("content%d",i))
                .date(LocalDateTime.now())
                .build();
    }
    public static Recommend returnRecommend(Gathering gathering){
        return Recommend.builder()
                .count(1L)
                .gathering(gathering)
                .build();
    }
    public static Like returnLike(User likedBy, Gathering gathering){
        return Like.builder()
                .likedBy(likedBy)
                .gathering(gathering)
                .build();
    }

}

package spring.myproject.util;

import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.category.Category;
import spring.myproject.domain.enrollment.Enrollment;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.GatheringCount;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.meeting.Meeting;
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
                                                 GatheringCount gatheringCount, List<Enrollment> enrollments){
        return Gathering.builder()
                .title(String.format("title%d",i))
                .content(String.format("content%d",i))
                .registerDate(LocalDateTime.now())
                .category(category)
                .createBy(createdBy)
                .gatheringImage(gatheringImage)
                .gatheringCount(gatheringCount)
                .enrollments(enrollments)
                .build();
    }
    public static Category returnDummyCategory(int i){
        return Category.builder()
                .name(String.format("category%d",i))
                .build();
    }
    public static GatheringCount returnDummyGatheringCount(int i){
        return GatheringCount.builder()
                .count(i)
                .build();
    }
    public static Enrollment returnDummyEnrollment(Gathering gathering,User enrolledBy){
        return Enrollment.builder()
                .date(LocalDateTime.now())
                .accepted(true)
                .gathering(gathering)
                .enrolledBy(enrolledBy)
                .build();
    }
    public static Meeting returnDummyMeeting(int i, User createdBy, List<Attend> attends, Gathering gathering){
        return Meeting.builder()
                .boardDate(LocalDateTime.now())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .title(String.format("title%d",i))
                .content(String.format("content%d",i))
                .createdBy(createdBy)
                .attends(attends)
                .gathering(gathering)
                .build();
    }
    public static Attend returnDummyAttend()

}

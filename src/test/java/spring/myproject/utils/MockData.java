package spring.myproject.utils;

import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.fcm.Topic;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.like.Like;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;

import java.time.LocalDateTime;

public class MockData {
    public static Image returnMockImage(String url){
        return Image.builder()
                .id(1L)
                .url(url)
                .build();
    }
    public static User returnMockUser(Long userId, String username, String password){
        return User.builder()
                .id(userId)
                .username(username)
                .password(password)
                .email("email")
                .address("address")
                .age(1)
                .hobby("hobby")
                .role(Role.USER)
                .nickname("nickname")
                .profileImage(returnMockImage("mock url"))
                .build();
    }
    public static String returnAccessToken(){
        return "accessToken";
    }
    public static String returnRefreshToken(){
        return "refreshToken";
    }
    public static Category returnMockCategory(String categoryName){
        return Category.builder()
                .id(1L)
                .name(categoryName)
                .build();
    }
    public static Gathering returnMockGathering(User user){
        return Gathering.builder()
                .id(1L)
                .createBy(user)
                .topic(returnMockTopic())
                .build();
    }
    public static Enrollment returnMockEnrollment(Gathering gathering, User enrolledBy){
        return Enrollment.builder()
                .gathering(gathering)
                .enrolledBy(enrolledBy)
                .build();
    }
    public static Topic returnMockTopic(){
        return Topic.builder()
                .topicName("topic Name")
                .build();
    }
    public static Like returnMockLike(Gathering gathering, User likedBy){
        return Like.builder()
                .gathering(gathering)
                .likedBy(likedBy)
                .build();
    }
    public static Meeting returnMockMeeting(Gathering gathering, User createdBy){
        return Meeting.builder()
                .createdBy(createdBy)
                .gathering(gathering)
                .meetingDate(LocalDateTime.now())
                .build();
    }
    public static Attend returnMockAttend(Meeting meeting, User attendBy){
        return Attend.builder()
                .meeting(meeting)
                .attendBy(attendBy)
                .build();
    }
    public static ChatRoom returnMockChatRoom(User createdBy){
        return ChatRoom.builder()
                .createdBy(createdBy)
                .build();
    }
    public static ChatParticipant returnMockChatParticipant(ChatRoom chatRoom, User user){
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
    }
}

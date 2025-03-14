package spring.myproject.util;

import spring.myproject.domain.Alarm;
import spring.myproject.domain.Attend;
import spring.myproject.domain.Board;
import spring.myproject.domain.Category;
import spring.myproject.domain.ChatMessage;
import spring.myproject.domain.ChatParticipant;
import spring.myproject.domain.ChatRoom;
import spring.myproject.domain.ReadStatus;
import spring.myproject.domain.Enrollment;
import spring.myproject.domain.Gathering;
import spring.myproject.domain.Image;
import spring.myproject.domain.Like;
import spring.myproject.domain.Meeting;
import spring.myproject.domain.Recommend;
import spring.myproject.domain.User;

import java.time.LocalDateTime;

import static spring.myproject.domain.Role.USER;

public class DummyData {

    public static Image returnDummyImage(int i){
        return Image.builder()
                .url(String.format("image%dUrl",i))
                .board(null)
                .build();
    }
    public static User returnDummyUser(int i, Image image){
        return User.builder()
                .username(String.format("user%d",i))
                .email(String.format("user%d@gmail.com",i))
                .hobby(String.format("hobby%d",i))
                .age(i)
                .role(USER)
                .nickname(String.format("nick%d",i))
                .address(String.format("address%d",i))
                .password("password")
                .email(String.format("email%d",i))
                .profileImage(image)
                .refreshToken(null)
                .build();
    }
    public static Gathering returnDummyGathering(int i, Category category, User createdBy, Image gatheringImage){
        return Gathering.builder()
                .title(String.format("title%d",i))
                .content(String.format("content%d",i))
                .registerDate(LocalDateTime.now())
                .category(category)
                .createBy(createdBy)
                .gatheringImage(gatheringImage)
                .count(i)
                .enrollments(null)
                .build();
    }
    public static Enrollment returnDummyEnrollment(User enrolledBy,Gathering gathering){
        return Enrollment.builder()
                .date(LocalDateTime.now())
                .accepted(true)
                .gathering(gathering)
                .enrolledBy(enrolledBy)
                .build();
    }
    public static Category returnDummyCategory(int i){
        return Category.builder()
                .name(String.format("category%d",i))
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
                .count(i)
                .image(null)
                .build();
    }
    public static Attend returnDummyAttend(User attendBy,Meeting meeting){
        return Attend.builder()
                .accepted(false)
                .meeting(meeting)
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
    public static Recommend returnDummyRecommend(Gathering gathering,int count){
        return Recommend.builder()
                .count(Long.valueOf(count))
                .gathering(gathering)
                .build();
    }
    public static Like returnDummyLike(User likedBy, Gathering gathering){
        return Like.builder()
                .likedBy(likedBy)
                .gathering(gathering)
                .build();
    }
    public static Board returnDummyBoard(User user, Gathering gathering,int i){
        return Board.builder()
                .user(user)
                .gathering(gathering)
                .title(String.format("title%d",i))
                .content(String.format("content%d",i))
                .description(String.format("description%d",i))
                .registerDate(LocalDateTime.now())
                .build();
    }
    public static ChatRoom returnDummyChatRoom(User createdBy, int i){
        return ChatRoom.builder()
                .count(1)
                .createdBy(createdBy)
                .name(String.format("name%d",i))
                .build();
    }
    public static ChatParticipant returnDummyChatParticipant(User user, ChatRoom chatRoom){
        return ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .status(true)
                .build();
    }
    public static ChatMessage returnDummyChatMessage(ChatRoom chatRoom,ChatParticipant chatParticipant,int i ){
        return ChatMessage.builder()
                .chatParticipant(chatParticipant)
                .chatRoom(chatRoom)
                .content(String.format("content%d",i))
                .build();
    }
    public static ReadStatus returnDummyReadStatus(ChatParticipant chatParticipant, ChatMessage chatMessage){
        return ReadStatus.builder()
                .chatParticipant(chatParticipant)
                .chatMessage(chatMessage)
                .status(false)
                .build();
    }


}

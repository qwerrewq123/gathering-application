package spring.myproject.utils;

import spring.myproject.entity.alarm.Alarm;
import spring.myproject.entity.attend.Attend;
import spring.myproject.entity.board.Board;
import spring.myproject.entity.category.Category;
import spring.myproject.entity.chat.ChatMessage;
import spring.myproject.entity.chat.ChatParticipant;
import spring.myproject.entity.chat.ChatRoom;
import spring.myproject.entity.chat.ReadStatus;
import spring.myproject.entity.enrollment.Enrollment;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.image.Image;
import spring.myproject.entity.like.Like;
import spring.myproject.entity.meeting.Meeting;
import spring.myproject.entity.recommend.Recommend;
import spring.myproject.entity.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static spring.myproject.entity.user.Role.USER;

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
                .nickname(String.format("nickname%d",i))
                .address(String.format("address%d",i))
                .password("password")
                .email(String.format("email%d",i))
                .profileImage(image)
                .build();
    }
    public static Gathering returnDummyGathering(int i, Category category, User createdBy, Image gatheringImage){
        return Gathering.builder()
                .title(String.format("title%d",i))
                .content(String.format("content%d",i))
                .registerDate(LocalDateTime.now())
                .createBy(createdBy)
                .gatheringImage(gatheringImage)
                .category(category)
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

    public static Meeting returnDummyMeeting(int i, User createdBy, Gathering gathering,Image image){
        return Meeting.builder()
                .meetingDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .title(String.format("title%d",i))
                .content(String.format("content%d",i))
                .createdBy(createdBy)
                .attends(null)
                .gathering(gathering)
                .count(i)
                .image(image)
                .build();
    }
    public static Attend returnDummyAttend(User attendBy,Meeting meeting){
        return Attend.builder()
                .meeting(meeting)
                .attendBy(attendBy)
                .date(LocalDateTime.now())
                .build();
    }
    public static Alarm returnDummyAlarm(int i, User user,boolean checked){
        return Alarm.builder()
                .user(user)
                .checked(checked)
                .content(String.format("content%d",i))
                .date(LocalDateTime.now())
                .build();
    }
    public static Recommend returnDummyRecommend(Gathering gathering,int score){
        return Recommend.builder()
                .score(Long.valueOf(score))
                .gathering(gathering)
                .date(LocalDate.now())
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
                .description(String.format("description%d",i))
                .registerDate(LocalDateTime.now())
                .images(new ArrayList<>())
                .build();
    }
    public static ChatRoom returnDummyChatRoom(User createdBy, Gathering gathering,int i){
        return ChatRoom.builder()
                .count(1)
                .createdBy(createdBy)
                .title(String.format("title%d",i))
                .description(String.format("description%d",i))
                .gathering(gathering)
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

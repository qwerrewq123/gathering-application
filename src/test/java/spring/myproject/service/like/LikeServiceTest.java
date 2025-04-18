package spring.myproject.service.like;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.entity.like.Like;
import spring.myproject.entity.user.Role;
import spring.myproject.entity.user.User;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.like.AlreadyLikeGatheringException;
import spring.myproject.common.exception.like.NotFoundLikeException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.repository.like.LikeRepository;
import spring.myproject.repository.user.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static spring.myproject.dto.response.like.LikeResponseDto.*;
import static spring.myproject.utils.ConstClass.SUCCESS_CODE;
import static spring.myproject.utils.ConstClass.SUCCESS_MESSAGE;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
    @Autowired
    LikeService likeService;
    @MockitoBean
    LikeRepository likeRepository;
    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    GatheringRepository gatheringRepository;

    @Test
    void like(){
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(gatheringRepository.findById(1L)).thenReturn(Optional.of(mock(Gathering.class)));
        when(gatheringRepository.findById(2L)).thenReturn(Optional.empty());
        when(likeRepository.findLike(eq(1L),anyLong())).thenReturn(Optional.empty());
        when(likeRepository.findLike(eq(2L),anyLong())).thenReturn(Optional.of(mock(Like.class)));

        assertThatThrownBy(()->likeService.like(2L,3L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->likeService.like(2L,2L))
                .isInstanceOf(NotFoundGatheringException.class);
        assertThatThrownBy(()->likeService.like(1L,2L))
                .isInstanceOf(AlreadyLikeGatheringException.class);
        LikeResponse likeResponse = likeService.like(1L, 1L);
        assertThat(likeResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    @Test
    void disLike(){
        User mockUser1 = new User(1L,"true username1","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        User mockUser2 = new User(2L,"true username2","password","email",
                "address",1,"hobby", Role.USER,"nickname",null,null,null);
        Like mockLike = Like.builder().likedBy(mockUser1).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(mockUser2));
        when(userRepository.findById(3L)).thenReturn(Optional.empty());
        when(gatheringRepository.findById(1L)).thenReturn(Optional.of(mock(Gathering.class)));
        when(gatheringRepository.findById(2L)).thenReturn(Optional.empty());
        when(likeRepository.findLike(eq(1L),anyLong())).thenReturn(Optional.of(mockLike));
        when(likeRepository.findLike(eq(2L),anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(()->likeService.dislike(2L,3L))
                .isInstanceOf(NotFoundUserException.class);
        assertThatThrownBy(()->likeService.dislike(2L,2L))
                .isInstanceOf(NotFoundGatheringException.class);
        assertThatThrownBy(()->likeService.dislike(1L,2L))
                .isInstanceOf(NotFoundLikeException.class);
        DislikeResponse disLikeResponse = likeService.dislike(1L, 1L);
        assertThat(disLikeResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

}

package spring.myproject.service.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
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
import spring.myproject.service.recommend.RecommendService;
import spring.myproject.utils.MockData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static spring.myproject.dto.response.like.LikeResponseDto.*;
import static spring.myproject.utils.ConstClass.SUCCESS_CODE;
import static spring.myproject.utils.ConstClass.SUCCESS_MESSAGE;
import static spring.myproject.utils.MockData.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {
    @InjectMocks
    LikeService likeService;
    @Mock
    LikeRepository likeRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    GatheringRepository gatheringRepository;
    @Mock
    RecommendService recommendService;

    @DisplayName("Throws NotFoundUserException")
    @Test
    void likeThrowsNotFoundUserException(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->likeService.like(1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }

    @DisplayName("Throws NotFoundGatheringException")
    @Test
    void likeThrowsNotFoundGatheringException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->likeService.like(1L,1L))
                .isInstanceOf(NotFoundGatheringException.class);
    }
    @DisplayName("Throws AlreadyLikeGatheringException")
    @Test
    void likeThrowsAlreadyLikeGatheringException(){
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        Like mockLike = returnMockLike(mockGathering,mockUser2);
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(mockUser2));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(likeRepository.findLike(2L,1L))
                .thenReturn(Optional.of(mockLike));
        assertThatThrownBy(()->likeService.like(1L,2L))
                .isInstanceOf(AlreadyLikeGatheringException.class);
    }
    @DisplayName("Return 200 Response")
    @Test
    void like(){
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(mockUser2));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(likeRepository.findLike(2L,1L))
                .thenReturn(Optional.empty());
        when(likeRepository.save(any(Like.class)))
                .thenReturn(mock(Like.class));
        doNothing().when(recommendService).addScore(anyLong(),anyInt());

        LikeResponse likeResponse = likeService.like(1L, 2L);

        assertThat(likeResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    @DisplayName("Throws NotFoundUserException")
    @Test
    void disLikeThrowsNotFoundUserException(){
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(()->likeService.like(1L,1L))
                .isInstanceOf(NotFoundUserException.class);
    }
    @DisplayName("Throws NotFoundGatheringException")
    @Test
    void disLikeThrowsNotFoundGatheringException(){
        User mockUser = returnMockUser(1L,"true username","true password");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(mockUser));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->likeService.like(1L,1L))
                .isInstanceOf(NotFoundGatheringException.class);
    }
    @DisplayName("Throws NotFoundLikeException")
    @Test
    void disLikeThrowsAlreadyLikeGatheringException(){
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(mockUser2));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(likeRepository.findLike(2L,1L))
                .thenReturn(Optional.empty());
        assertThatThrownBy(()->likeService.dislike(1L,2L))
                .isInstanceOf(NotFoundLikeException.class);
    }

    @Test
    void disLike(){
        User mockUser1 = returnMockUser(1L,"true username","true password");
        User mockUser2 = returnMockUser(2L,"true username","true password");
        Gathering mockGathering = returnMockGathering(mockUser1);
        Like mockLike = returnMockLike(mockGathering,mockUser2);
        when(userRepository.findById(2L))
                .thenReturn(Optional.of(mockUser2));
        when(gatheringRepository.findById(1L))
                .thenReturn(Optional.of(mockGathering));
        when(likeRepository.findLike(2L,1L))
                .thenReturn(Optional.of(mockLike));
        doNothing().when(likeRepository).delete(any(Like.class));
        doNothing().when(recommendService).addScore(anyLong(),anyInt());

        DislikeResponse disLikeResponse = likeService.dislike(1L, 2L);

        assertThat(disLikeResponse)
                .extracting("code","message")
                .containsExactly(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

}

package spring.myproject.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.entity.gathering.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.entity.like.Like;
import spring.myproject.repository.like.LikeRepository;
import spring.myproject.common.exception.meeting.NotAuthorizeException;
import spring.myproject.entity.user.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.common.exception.gathering.NotFoundGatheringException;
import spring.myproject.common.exception.like.AlreadyLikeGatheringException;
import spring.myproject.common.exception.like.NotFoundLikeException;
import spring.myproject.common.exception.user.NotFoundUserException;
import spring.myproject.service.recommend.RecommendService;

import java.util.Optional;

import static spring.myproject.dto.response.like.LikeResponseDto.*;
import static spring.myproject.utils.ConstClass.*;


@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;
    private final RecommendService recommendService;

    public LikeResponse like(Long gatheringId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Long userId = user.getId();
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()-> new NotFoundGatheringException("no exist Gathering!!"));
            Optional<Like> optionalLike = likeRepository.findLike(userId, gatheringId);
            if(optionalLike.isPresent()) throw new AlreadyLikeGatheringException("Already Like Gathering!!");
            likeRepository.save(Like.of(gathering,user));
            recommendService.addScore(gatheringId,1);
            return LikeResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public DislikeResponse dislike(Long gatheringId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Long userId = user.getId();
            gatheringRepository.findById(gatheringId).orElseThrow(()-> new NotFoundGatheringException("no exist Gathering!!"));
            Like like = likeRepository.findLike(userId, gatheringId).orElseThrow(()-> new NotFoundLikeException("no exist Like"));
            Long likedId = like.getLikedBy().getId();
            if(!likedId.equals(userId)) throw new NotAuthorizeException("no Authorize!");
            likeRepository.delete(like);
            recommendService.addScore(gatheringId,-1);
            return DislikeResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

}

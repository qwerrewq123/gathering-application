package spring.myproject.service.like;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.Gathering;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.domain.Like;
import spring.myproject.repository.like.LikeRepository;
import spring.myproject.exception.meeting.NotAuthorizeException;
import spring.myproject.domain.User;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.dto.response.like.DislikeResponse;
import spring.myproject.dto.response.like.LikeResponse;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.exception.like.AlreadyLikeGathering;
import spring.myproject.exception.like.NotFoundLikeException;
import spring.myproject.exception.user.NotFoundUserException;

import java.util.Optional;

import static spring.myproject.util.ConstClass.*;


@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;

    public LikeResponse like(Long gatheringId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Long userId = user.getId();
            Optional<Like> optionalLike = likeRepository.findLike(userId, gatheringId);
            Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(()-> new NotFoundGatheringException("no exist Gathering!!"));
            if(optionalLike.isPresent()) throw new AlreadyLikeGathering("Already Like Gathering!!");
            likeRepository.save(Like.of(gathering,user));
            return LikeResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
    public DislikeResponse dislike(Long gatheringId, String username) {

            User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
            Long userId = user.getId();
            Like like = likeRepository.findLike(userId, gatheringId).orElseThrow(()-> new NotFoundLikeException("no exist Like"));
            gatheringRepository.findById(gatheringId).orElseThrow(()-> new NotFoundGatheringException("no exist Gathering!!"));
            Long likedId = like.getLikedBy().getId();
            if(likedId.equals(userId)) throw new NotAuthorizeException("no Authorize!1");
            likeRepository.delete(like);
            return DislikeResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }

}

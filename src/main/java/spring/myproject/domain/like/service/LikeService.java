package spring.myproject.domain.like.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.myproject.domain.gathering.Gathering;
import spring.myproject.domain.gathering.repository.GatheringRepository;
import spring.myproject.domain.like.Like;
import spring.myproject.domain.like.repository.LikeRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.domain.like.dto.response.DislikeResponse;
import spring.myproject.domain.like.dto.response.LikeResponse;
import spring.myproject.domain.gathering.exception.NotFoundGatheringException;
import spring.myproject.domain.like.exception.AlreadyLikeGathering;
import spring.myproject.domain.like.exception.NotFoundLikeException;
import spring.myproject.domain.user.exception.NotFoundUserException;

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
            likeRepository.delete(like);
            return DislikeResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE);
    }
}

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

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final GatheringRepository gatheringRepository;

    public void like(Long gatheringId, String username) {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }
        Long userId = user.getId();
        Like like = likeRepository.findLike(userId, gatheringId);
        Gathering gathering = gatheringRepository.findById(gatheringId).orElse(null);
        if(gathering == null){
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        }
        if(like != null){
            throw new IllegalArgumentException("이미 좋아요를 누른적이 있습니다");
        }

        likeRepository.save(Like.builder()
                .gathering(gathering)
                .likedBy(user)
                .build());


    }

    public void dislike(Long gatheringId, String username) {

        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당하는 유저가 없습니다");
        }
        Long userId = user.getId();
        Like like = likeRepository.findLike(userId, gatheringId);
        Gathering gathering = gatheringRepository.findById(gatheringId).orElse(null);
        if(gathering == null){
            throw new IllegalArgumentException("해당하는 소모임이 없습니다");
        }
        if(like == null){
            throw new IllegalArgumentException("해당하는 모임을 좋아요를 누른적이 없습니다");
        }

        likeRepository.delete(like);


    }
}

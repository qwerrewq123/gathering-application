package spring.myproject.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByUsername(String username);

    Boolean existsByNickname(String nickname);

    @Query("select u from User u where u.email = :email")
    List<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.profileImage where u.id = :userId")
    Optional<User> findById(Long userId);

    @Query("select u from User u left join fetch FCMToken t on t.user.id = u.id where u.id =:userId")
    Optional<User> findAndTokenByUserId(Long userId);

    @Query("select u from User u join fetch FCMToken t on t.user.id = u.id")
    Optional<User> findByUsername(String username);
}

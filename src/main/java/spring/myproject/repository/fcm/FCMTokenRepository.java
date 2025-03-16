package spring.myproject.repository.fcm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.myproject.entity.fcm.FCMToken;
import spring.myproject.entity.user.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FCMTokenRepository extends JpaRepository<FCMToken, Long> {

    @Query("select t from FCMToken t join t.user u where t.tokenValue =:tokenValue and u.id =:userId")
    Optional<FCMToken> findByTokenAndUser(String tokenValue, Long userid);

    @Modifying
    @Query("delete from FCMToken t where t.tokenValue in :failedTokens")
    void deleteByTokenValueIn(@Param("failedTokens") List<String> failedTokens);

    @Query("select t from FCMToken t where t.expirationDate > :now")
    List<FCMToken> findByExpirationDate(LocalDate now);
}

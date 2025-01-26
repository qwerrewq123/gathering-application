package spring.myproject.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Boolean existsByUsername(String username);

}

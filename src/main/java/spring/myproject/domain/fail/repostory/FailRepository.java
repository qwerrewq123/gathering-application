package spring.myproject.domain.fail.repostory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.fail.Fail;

import java.util.Optional;

public interface FailRepository extends JpaRepository<Fail,Long> {

}

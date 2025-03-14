package spring.myproject.repository.fail;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.Fail;

public interface FailRepository extends JpaRepository<Fail,Long> {

}

package spring.myproject.domain.attend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.attend.Attend;
import spring.myproject.domain.user.User;

import java.util.List;

public interface AttendRepository extends JpaRepository<Attend,Long> {

}

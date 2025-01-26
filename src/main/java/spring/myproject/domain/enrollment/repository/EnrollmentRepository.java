package spring.myproject.domain.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.myproject.domain.enrollment.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {
}

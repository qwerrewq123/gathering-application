package spring.myproject.domain.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.enrollment.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    @Query("select e from Enrollment e where e.gathering.id = :gatheringId and e.enrolledBy.username = :userId")
    Boolean existEnrollment(Long gatheringId,Long userId);

    @Query("select e from Enrollment e where e.gathering.id = :gatheringId and e.enrolledBy.username = :userId and e.accepted = true")
    Enrollment findEnrollment(Long gatheringId,Long userId);


}

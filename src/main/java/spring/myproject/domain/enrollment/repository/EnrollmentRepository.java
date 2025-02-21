package spring.myproject.domain.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import spring.myproject.domain.enrollment.Enrollment;

import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    @Query("select e from Enrollment e where e.gathering.id = :gatheringId and e.enrolledBy.id = :userId")
    Enrollment existEnrollment(Long gatheringId,Long userId);


    @Query("select e from Enrollment e join e.enrolledBy u " +
            "where e.gathering.id = :gatheringId and u.id = :userId and e.accepted = true")
    Optional<Enrollment> findEnrollment(Long gatheringId, Long userId);


}

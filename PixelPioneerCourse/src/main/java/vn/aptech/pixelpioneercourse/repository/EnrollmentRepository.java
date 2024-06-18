package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    Enrollment findFirstByUserOrderByEnrolledAtDesc(User user);
    List<Enrollment> findEnrollmentsByUserId(Integer userId);
    Optional<Enrollment> findByUserId(Integer userId);
    boolean existsByUserIdAndPaymentStatusTrue(Integer userId);
}

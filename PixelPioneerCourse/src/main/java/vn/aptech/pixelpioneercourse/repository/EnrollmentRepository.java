package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {

    Enrollment findFirstByUserOrderByEnrolledAtDesc(User user);

    List<Enrollment> findEnrollmentsByUserId(Integer userId);

    Optional<Enrollment> findByUserId(Integer userId);

    boolean existsByUserIdAndPaymentStatusTrue(Integer userId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.subscriptionType = 'MONTHLY' AND e.enrolledAt BETWEEN :startDate AND :endDate")
    Integer countMonthlyEnrollments(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.subscriptionType = 'YEARLY' AND e.enrolledAt BETWEEN :startDate AND :endDate")
    Integer countYearlyEnrollments(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.subscriptionType = 'UNLIMITED' AND e.enrolledAt BETWEEN :startDate AND :endDate")
    Integer countUnlimitedEnrollments(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.subscriptionType = 'MONTHLY'")
    Integer countAllBySubscriptionTypeMonthly();

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.subscriptionType = 'YEARLY'")
    Integer countAllBySubscriptionTypeYearly();

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.subscriptionType = 'UNLIMITED'")
    Integer countAllBySubscriptionTypeUnlimited();

    @Query("SELECT e.enrolledAt AS date, " +
            "SUM(CASE WHEN e.subscriptionType = 'MONTHLY' THEN 1 ELSE 0 END) AS countMonthly, " +
            "SUM(CASE WHEN e.subscriptionType = 'YEARLY' THEN 1 ELSE 0 END) AS countYearly, " +
            "SUM(CASE WHEN e.subscriptionType = 'UNLIMITED' THEN 1 ELSE 0 END) AS countUnlimited " +
            "FROM Enrollment e " +
            "WHERE e.enrolledAt BETWEEN :startDate AND :endDate " +
            "GROUP BY e.enrolledAt")
    List<Object[]> findDailySalesBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    List<Enrollment> findTop10ByOrderByEnrolledAtDesc();
    
    List<Enrollment> findAllByUserId(Integer id, Sort sort);
}

    

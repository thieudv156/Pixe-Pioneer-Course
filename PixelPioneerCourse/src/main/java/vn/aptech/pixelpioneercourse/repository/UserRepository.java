package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import vn.aptech.pixelpioneercourse.dto.UserInformation;
import vn.aptech.pixelpioneercourse.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByEmail(String EmailorUsername);

    Optional<User> findById(int id);
    
    Optional<User> findByUsername(String EmailorUsername);
    
    Optional<User> findByPassword(String password);
    
    Optional<User> findByPhone(String phone);
    
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword% OR u.fullName LIKE %:keyword%")
    List<User> searchByKeyword(@Param("keyword") String keyword);
    @Query("SELECT u FROM User u WHERE u.role.roleName = 'ROLE_INSTRUCTOR'")
    List<UserInformation> findAllInstructor();

    List<User> findAllByActiveStatusIsTrue();


    @Query("SELECT DISTINCT u FROM User u JOIN u.progresses p JOIN p.subLesson sl JOIN sl.lesson l JOIN l.course c WHERE c.instructor.id = :instructorId AND u.createdAt = :today")
    List<User> findNewEnrollmentsByInstructorId(@Param("instructorId") Integer instructorId, @Param("today") LocalDate today);

    @Query("SELECT DISTINCT u FROM User u JOIN u.courseCompletes cc JOIN cc.course c WHERE c.instructor.id = :instructorId AND DATE(cc.completedDate) = :today")
    List<User> findCompletedCoursesByInstructorId(@Param("instructorId") Integer instructorId, @Param("today") LocalDate today);

    @Query("SELECT DISTINCT u FROM User u JOIN u.progresses p JOIN p.subLesson sl JOIN sl.lesson l JOIN l.course c WHERE c.instructor.id = :instructorId")
    List<User> findAllEnrollmentsByInstructorId(@Param("instructorId") Integer instructorId);

}

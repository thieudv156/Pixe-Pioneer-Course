package vn.aptech.pixelpioneercourse.repository;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import vn.aptech.pixelpioneercourse.dto.UserInformation;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByIsPublishedIsTrue();
    List<Course> findByCategoryId(Integer categoryId);
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByInstructorId(Integer instructorId);
    List<Course> findByInstructorIdAndIsPublishedIsTrue(Integer instructorId);
    List<Course> findByInstructorIdAndIsPublishedIsFalse(Integer instructorId);
    @Query("SELECT u FROM User u WHERE u.id = (SELECT c.instructor.id FROM Course c WHERE c.title = :title)")
    Optional<User> findInstructorNameByTitle(@Param("title") String title);
}

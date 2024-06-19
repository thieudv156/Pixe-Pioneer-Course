package vn.aptech.pixelpioneercourse.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.pixelpioneercourse.entities.Course;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByIsPublishedIsTrue();
    List<Course> findByCategoryId(Integer categoryId);
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByInstructorId(Integer instructorId);
    List<Course> findByInstructorIdAndIsPublishedIsTrue(Integer instructorId);
    List<Course> findByInstructorIdAndIsPublishedIsFalse(Integer instructorId);

    List<Course> findDistinctByLessons_SubLessons_Progresses_User_Id(Integer userId);
}

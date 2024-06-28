package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.Course;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    List<Course> findByIsPublishedIsTrue();

    @Query("SELECT c FROM Course c WHERE c.category.id = :categoryId")
    List<Course> findByCategoryId(@Param("categoryId") Integer categoryId);

    List<Course> findByTitleContainingIgnoreCase(String title);

    List<Course> findByInstructorId(Integer instructorId);

    List<Course> findByInstructorIdAndIsPublishedIsTrue(Integer instructorId);

    List<Course> findByInstructorIdAndIsPublishedIsFalse(Integer instructorId);

    List<Course> findDistinctByLessons_SubLessons_Progresses_User_Id(Integer userId);

    List<Course> findTop8ByCategoryAndIsPublishedIsTrueOrderByCreatedAtDesc(Category category);

    @Query("SELECT COUNT(DISTINCT p.user) FROM Progress p JOIN p.subLesson sl JOIN sl.lesson l WHERE l.course.id = :courseId")
    Integer countDistinctUsersByCourseId(@Param("courseId") Integer courseId);

    @Query("SELECT COUNT(DISTINCT cc.user) FROM CourseComplete cc WHERE cc.course.id = :courseId")
    Integer countDistinctUsersByCourseComplete(@Param("courseId") Integer courseId);
}

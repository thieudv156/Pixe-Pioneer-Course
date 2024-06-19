package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.aptech.pixelpioneercourse.entities.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Integer>{
    Optional<Progress> findBySubLessonIdAndUserId(Integer subLessonId, Integer userId);

    Optional<Progress> findByUserIdAndSubLessonId(Integer userId, Integer id);


    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user.id = :userId AND p.subLesson.lesson.course.id = :courseId")
    Integer countTotalSubLessonsByCourseAndUser(@Param("courseId") Integer courseId, @Param("userId") Integer userId);

    @Query("SELECT COUNT(p) FROM Progress p WHERE p.user.id = :userId AND p.subLesson.lesson.course.id = :courseId AND p.isCompleted = true")
    Integer countCompletedSubLessonsByCourseAndUser(@Param("courseId") Integer courseId, @Param("userId") Integer userId);


    @Query("SELECT p.subLesson FROM Progress p WHERE p.user.id = :userId AND p.subLesson.lesson.course.id = :courseId AND p.isCompleted = false ORDER BY p.id ASC")
    List<SubLesson> findFirstIncompleteSubLessonByUserIdAndCourseId(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    @Query("SELECT DISTINCT p.user FROM Progress p WHERE p.subLesson.lesson.course.id = :courseId")
    List<User> findUsersByCourseId(@Param("courseId") Integer courseId);


}

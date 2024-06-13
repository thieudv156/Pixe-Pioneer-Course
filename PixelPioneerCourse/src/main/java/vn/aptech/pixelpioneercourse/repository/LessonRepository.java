package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer>{
    List<Lesson> findByCourseIdOrderByOrderNumber(Integer courseId);
    List<Lesson> findByCourseIdAndTitleContainingIgnoreCase(Integer courseId, String title);


}

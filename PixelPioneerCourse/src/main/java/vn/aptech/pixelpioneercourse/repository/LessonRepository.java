package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Lesson;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer>{
    List<Lesson> findByTitleContainingIgnoreCase(String title);
    List<Lesson> findByCourseId(int courseId);

}

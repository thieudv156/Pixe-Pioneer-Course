package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Quiz;

import java.util.List;


public interface QuizRepository extends JpaRepository<Quiz, Integer>{

    List<Quiz> findByLessonId(Integer lessonId);
}
package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.CourseComplete;

public interface CourseCompleteRepository extends JpaRepository<CourseComplete, Integer> {
    CourseComplete findByUserIdAndCourseId(Integer userId, Integer courseId);
}

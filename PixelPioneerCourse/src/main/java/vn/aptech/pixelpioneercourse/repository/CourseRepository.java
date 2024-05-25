package vn.aptech.pixelpioneercourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Course;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}

package vn.aptech.pixelpioneercourse.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {


}

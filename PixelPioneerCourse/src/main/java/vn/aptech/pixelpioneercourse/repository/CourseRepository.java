package vn.aptech.pixelpioneercourse.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {


    List<Course> findByCategory(Category category);

    List<Course> findByInstructor(Instructor instructor);

    List<Course> findByTitleContainingIgnoreCase(String title);
}

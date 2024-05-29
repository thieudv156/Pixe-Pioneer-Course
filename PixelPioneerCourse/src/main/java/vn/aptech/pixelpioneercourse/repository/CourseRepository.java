package vn.aptech.pixelpioneercourse.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {


    List<Course> findByCategoryId(int categoryId);
    List<Course> findByTitleContainingIgnoreCase(String title);
    List<Course> findByInstructorId(int instructorId);

}

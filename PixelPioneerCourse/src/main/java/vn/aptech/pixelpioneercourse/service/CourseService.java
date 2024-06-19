package vn.aptech.pixelpioneercourse.service;

import org.springframework.data.relational.core.sql.In;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.SubLesson;

import java.util.List;

public interface  CourseService {
    List<Course> findAll();
    Course findById(Integer id);
    Course update(Integer id, CourseCreateDto dto,MultipartFile image);
    boolean delete(Integer id);
    List<Course> findByCategoryId(Integer categoryId);
    List<Course> findByInstructorId(Integer instructorId);
    List<Course> findByTitle(String title);
    List<Course> findAllPublishedCourses();
    List<Course> findPublishedCoursesByInstructorId(Integer instructorId);
    List<Course> findUnPublishedCoursesByInstructorId(Integer instructorId);
    Course publishCourse(Integer courseId);
    Course unpublishCourse(Integer courseId);
    Course createNewCourse(Integer userId);
    Boolean startCourse(Integer courseId, Integer userId);
    boolean canAccessCourse(Integer userId, Integer courseId);
    List<Course> getEnrolledCourses(Integer userId);
}

package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAll();
    Course findById(int id);
    boolean update(int id, CourseCreateDto dto);
    boolean save(CourseCreateDto dto);
    boolean delete(int id);
    List<Course> findByCategoryId(int categoryId);
    List<Course> findByUserInstructorId(int instructorId);
    List<Course> findByTitle(String title);
}

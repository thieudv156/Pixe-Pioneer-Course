package vn.aptech.pixelpioneercourse.controller.api.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.service.CategoryService;
import vn.aptech.pixelpioneercourse.service.CourseService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CourseController(CourseService courseService, CategoryService categoryService) {
        this.courseService = courseService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> index(){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findAllPublishedCourses());
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Integer id){
        try {
            Optional<Course> result = Optional.ofNullable(courseService.findById(id));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> findCourseByCategoryId(@PathVariable("categoryId") Integer categoryId){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findByCategoryId(categoryId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

   @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<?> findCourseByInstructorId(@PathVariable("instructorId") Integer instructorId){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findByInstructorId(instructorId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<?> findByTitle(@PathVariable("title") String keyword){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findByTitle(keyword));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/create")
    public ResponseEntity<?> create() {
        try {
            Map<Integer, String> categories = new HashMap<>();
            categoryService.findAll().forEach(c -> categories.put(c.getId(), c.getName()));

            Map<String, Object> response = new HashMap<>();
            response.put("categories", categories);
            response.put("course", new CourseCreateDto());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> create(
            @RequestParam("image") MultipartFile image,
            @RequestParam("courseData") String courseData) {

        try {
            // Convert courseData (JSON string) to CourseCreateDto
            CourseCreateDto courseCreateDto = objectMapper.readValue(courseData, CourseCreateDto.class);

            // Set the image file to the DTO
            courseCreateDto.setImage(image);

            // Save the course
            Course savedCourse = courseService.save(courseCreateDto);
            if (savedCourse != null) {
                return ResponseEntity.ok(savedCourse);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating course");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(
            @PathVariable("id") Integer id,
            @RequestParam("image") MultipartFile image,
            @RequestParam("courseData") String courseData) {

        try {
            // Convert courseData (JSON string) to CourseCreateDto
            CourseCreateDto courseCreateDto = objectMapper.readValue(courseData, CourseCreateDto.class);

            // Set the image file to the DTO
            courseCreateDto.setImage(image);

            // Update the course
            if (courseService.update(id, courseCreateDto)) {
                return ResponseEntity.ok("Course updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
        try {
            if (courseService.delete(id)) {
                return ResponseEntity.ok("Course deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<?> publishCourse(@PathVariable("id") Integer id) {
        try {
            Course course = courseService.publishCourse(id);
            if (course != null) {
                return ResponseEntity.ok(course);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/unpublished")
    public ResponseEntity<?> unpublishedCourse(@PathVariable("id") Integer id) {
        try {
            Course course = courseService.unpublishCourse(id);
            if (course != null) {
                return ResponseEntity.ok(course);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/instructors/{instructorId}/published")
    public ResponseEntity<?> findPublishedCoursesByInstructorId(@PathVariable("instructorId") Integer instructorId){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findPublishedCoursesByInstructorId(instructorId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/instructors/{instructorId}/unpublished")
    public ResponseEntity<?> findUnPublishedCoursesByInstructorId(@PathVariable("instructorId") Integer instructorId){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findUnPublishedCoursesByInstructorId(instructorId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}

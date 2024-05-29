package vn.aptech.pixelpioneercourse.controller.api.course;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.service.CategoryService;
import vn.aptech.pixelpioneercourse.service.CourseService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;
    private final CategoryService categoryService;

    public CourseController(CourseService courseService, CategoryService categoryService) {
        this.courseService = courseService;
        this.categoryService = categoryService;
    }

    @GetMapping("")
    public ResponseEntity<?> index(){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findAll());
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") int id){
        try {
            Optional<Course> result = Optional.ofNullable(courseService.findById(id));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<?> findCourseByCategoryId(@PathVariable("categoryId") int categoryId){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findByCategoryId(categoryId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

   @GetMapping("/instructors/{instructorId}")
    public ResponseEntity<?> findCourseByInstructorId(@PathVariable("instructorId") int instructorId){
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

    @PostMapping("/create")
public ResponseEntity<?> create(@Valid @RequestBody CourseCreateDto courseCreateDto) {
    try {
        if ( courseService.save(courseCreateDto)) {
            return ResponseEntity.ok("Course created successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating course");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
    }
}

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CourseCreateDto courseCreateDto, @PathVariable("id") int id) {
        try {
            if (courseService.update(id, courseCreateDto)) {
                return ResponseEntity.ok("Course updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
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

}

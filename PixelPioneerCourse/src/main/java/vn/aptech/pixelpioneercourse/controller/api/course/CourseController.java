package vn.aptech.pixelpioneercourse.controller.api.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.Role;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import org.springframework.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.dto.TestFormatCreateDto;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.*;
import vn.aptech.pixelpioneercourse.until.ControllerUtils;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Validator validator;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ProgressService progressService;
    private final TestFormatService testFormatService;

    public CourseController(CourseService courseService, CategoryService categoryService, Validator validator, UserService userService, ModelMapper modelMapper, ProgressService progressService1, TestFormatService testFormatService) {
        this.courseService = courseService;
        this.categoryService = categoryService;
        this.validator = validator;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.progressService = progressService1;
        this.testFormatService = testFormatService;
    }



    @GetMapping("")
    public ResponseEntity<?> index() {
        try {
            List<Course> courses = courseService.findAllPublishedCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/instructors")
    public ResponseEntity<?> getAllInstructors() {
        try {
            List<User> instructors = userService.findAll();
            for (int i = 0; i < instructors.size(); i++) {
                if (!instructors.get(i).getRole().equals(modelMapper.map("ROLE_INSTRUCTOR", Role.class))) {
                    instructors.remove(i);
                }
            }
            return ResponseEntity.ok(instructors);
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
    
    @GetMapping("/search-by-name")
    public ResponseEntity<?> findCourseByName(@RequestParam("courseName") String courseName) {
    	try {
    		return ResponseEntity.status(HttpStatus.OK).body(courseService.findByTitle(courseName));
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    	}
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<?> findCourseByCategoryId(@PathVariable("categoryId") Integer categoryId){
        try {
            Optional<List<Course>> result = Optional.ofNullable(courseService.findByCategoryId(categoryId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/categoryDto/{categoryId}")
    public ResponseEntity<?> findCourseByCategoryDtoId(@PathVariable("categoryId") Integer categoryId){
        try {
            List<Course> courses = courseService.findByCategoryId(categoryId);
            return ResponseEntity.ok(courses);
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

    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(
            @PathVariable("id") Integer id,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "courseData") String courseData) {

        try {
            // Convert courseData (JSON string) to CourseCreateDto
            CourseCreateDto courseCreateDto = objectMapper.readValue(courseData, CourseCreateDto.class);

            // Set the image file to the DTO
            BindingResult bindingResult = new BeanPropertyBindingResult(courseCreateDto, "courseCreateDto");
            validator.validate(courseCreateDto, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ControllerUtils.getErrorMessages(bindingResult));
            }
            Course updatedCourse = courseService.update(id, courseCreateDto, image);
            if (updatedCourse != null) {
                return ResponseEntity.ok(updatedCourse);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error updating course");
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

    @GetMapping("/create-course/{instructorId}")
    public ResponseEntity<?> createNewCourse(@PathVariable("instructorId") Integer instructorId){
        try {
            Optional<Course> course = Optional.ofNullable(courseService.createNewCourse(instructorId));
            return course.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories(){
        try {
            Optional<List<Category>> result = Optional.ofNullable(categoryService.findAll());
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{courseId}/{userId}/start-progress")
    public ResponseEntity<?> startCourseProgress(@PathVariable("courseId") Integer courseId, @PathVariable("userId") Integer userId) {
    	try {
    		return ResponseEntity.status(HttpStatus.OK).body(courseService.startCourse(courseId,userId));
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    	}
    }

    @GetMapping("/{courseId}/{userId}/get-sub-lesson-progress")
    public ResponseEntity<?> getCourseSubLessonProgress(@PathVariable("courseId") Integer courseId, @PathVariable Integer userId){
    	try {
    		return ResponseEntity.status(HttpStatus.OK).body(progressService.getCurrentSubLessonByCourseId(courseId,userId));
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    	}
    }

    @GetMapping("/{courseId}/{userId}/get-progress")
    public ResponseEntity<?> getCourseProgress(@PathVariable("courseId") Integer courseId, @PathVariable Integer userId){
    	try {
    		return ResponseEntity.status(HttpStatus.OK).body(progressService.getCurrentProgressByCourseId(courseId,userId));
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    	}
    }

    @GetMapping("/{userId}/get-enrolled-course")
    public ResponseEntity<?> getEnrolledCourse(@PathVariable("userId") Integer userId){
    	try {
    		return ResponseEntity.status(HttpStatus.OK).body(courseService.getEnrolledCourses(userId));
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    	}
    }

    @GetMapping("/{courseId}/create-test-format")
    public ResponseEntity<?> createTestFormat(@PathVariable("courseId") Integer courseId, @Valid @RequestBody TestFormatCreateDto testFormatCreateDto){
    	try {
    		return ResponseEntity.status(HttpStatus.OK).body(testFormatService.save(testFormatCreateDto));
    	} catch (Exception e) {
    		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    	}
    }


}
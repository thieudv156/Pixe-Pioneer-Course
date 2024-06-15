package vn.aptech.pixelpioneercourse.controller.api.lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.LessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Lesson;
import vn.aptech.pixelpioneercourse.service.LessonService;
import vn.aptech.pixelpioneercourse.until.ControllerUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    final private LessonService lessonService;
    private final ObjectMapper objectMapper;
    private final Validator validator;


    public LessonController(LessonService lessonService, ObjectMapper objectMapper, Validator validator) {
        this.lessonService = lessonService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> findLessonByCourseId(@PathVariable("courseId") Integer courseId){
        try {
            Optional<List<Lesson>> result = Optional.ofNullable(lessonService.findAllLessonByCourseId(courseId));

            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

   @GetMapping("/{lessonId}")
    public ResponseEntity<?> findLessonById(@PathVariable("lessonId") Integer lessonId){
        try {
            Optional<Lesson> result = Optional.ofNullable(lessonService.findById(lessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{lessonId}/update")
    public ResponseEntity<?> updateLesson(@PathVariable("lessonId") Integer lessonId, @RequestBody LessonCreateDto dto){
        try {
            Optional<Lesson> result = Optional.ofNullable(lessonService.update(lessonId, dto));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{lessonId}/delete")
    public ResponseEntity<?> deleteLesson(@PathVariable("lessonId") Integer lessonId){
        try {
            Optional<Integer> result = Optional.of(lessonService.delete(lessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{courseId}/create-lesson")
    public ResponseEntity<?> createLesson(@PathVariable("courseId") Integer courseId){
        try {
            Optional<Lesson> lesson = Optional.ofNullable(lessonService.createNewLesson(courseId));
            return lesson.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }



}

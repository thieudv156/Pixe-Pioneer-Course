package vn.aptech.pixelpioneercourse.controller.api.lesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.LessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Lesson;
import vn.aptech.pixelpioneercourse.service.LessonService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    final private LessonService lessonService;
    private final ObjectMapper objectMapper;


    public LessonController(LessonService lessonService, ObjectMapper objectMapper) {
        this.lessonService = lessonService;
        this.objectMapper = objectMapper;
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

    @PutMapping("/{lessonId}/status/complete")
    public ResponseEntity<?> completeLesson(@PathVariable("lessonId") Integer lessonId){
        try {
            Optional<Lesson> result = Optional.ofNullable(lessonService.completeLesson(lessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{lessonId}/update")
    public ResponseEntity<?> updateLesson(@PathVariable("lessonId") Integer lessonId,
                                          @RequestParam("image") MultipartFile image,
                                          @RequestPart("lessonData") String lessonData) {
        try {
            LessonCreateDto dto = objectMapper.readValue(lessonData, LessonCreateDto.class);
            dto.setImage(image);
            Optional<Lesson> result = Optional.ofNullable(lessonService.update(lessonId, dto));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing lesson data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{lessonId}/delete")
    public ResponseEntity<?> deleteLesson(@PathVariable("lessonId") Integer lessonId){
        try {
            Optional<Boolean> result = Optional.of(lessonService.delete(lessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createLesson(@RequestParam("image") MultipartFile image,
                                          @RequestPart("lessonData") String lessonData) {
        try {
            LessonCreateDto dto = objectMapper.readValue(lessonData, LessonCreateDto.class);
            dto.setImage(image);
            Optional<Lesson> result = Optional.ofNullable(lessonService.save(dto));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing lesson data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

}

package vn.aptech.pixelpioneercourse.controller.api.lesson;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.aptech.pixelpioneercourse.entities.Lesson;
import vn.aptech.pixelpioneercourse.service.LessonService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/course/{courseId}/lessons")
public class LessonController {

    final private LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping("")
    public ResponseEntity<?> findLessonByCourseId(@PathVariable("courseId") Integer courseId){
        try {
            Optional<List<Lesson>> result = Optional.ofNullable(lessonService.findAllLessonByCourseId(courseId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}

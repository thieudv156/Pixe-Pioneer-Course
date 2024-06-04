package vn.aptech.pixelpioneercourse.controller.api.quiz;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.QuizCreateDto;
import vn.aptech.pixelpioneercourse.entities.Quiz;
import vn.aptech.pixelpioneercourse.service.QuizService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?> findQuizByLessonId(@PathVariable("lessonId") Integer lessonId){
        try {
            Optional<List<Quiz>> result = Optional.ofNullable(quizService.findAllQuizByLessonId(lessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{quizId}")
    public ResponseEntity<?> findQuizById(@PathVariable("quizId") Integer quizId){
        try {
            Optional<Quiz> result = Optional.ofNullable(quizService.findById(quizId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestBody QuizCreateDto dto){
        try {
            Optional<Quiz> result = Optional.ofNullable(quizService.save(dto));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{quizId}/update")
    public ResponseEntity<?> updateQuiz(@PathVariable("quizId") Integer quizId, @RequestBody QuizCreateDto dto){
        try {
            Optional<Quiz> result = Optional.ofNullable(quizService.update(quizId, dto));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{quizId}/delete")
    public ResponseEntity<?> deleteQuiz(@PathVariable("quizId") Integer quizId){
        try {
            Optional<Boolean> result = Optional.of(quizService.delete(quizId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/{quizId}/check")
    public ResponseEntity<?> checkAnswer(@PathVariable("quizId") Integer quizId, @RequestParam("answer") String answer){
        try {
            Optional<Boolean> result = Optional.of(quizService.checkAnswer(quizId, answer));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

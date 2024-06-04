package vn.aptech.pixelpioneercourse.controller.api.test;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.entities.Test;
import vn.aptech.pixelpioneercourse.service.TestService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/test")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/lesson/{lessonId}/create-test")
    public ResponseEntity<?> createTest(@PathVariable("lessonId") Integer lessonId){
        try{
            Optional<Test> test = Optional.ofNullable(testService.createTest(lessonId));
            return test.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/{testId}/complete-test")
    public ResponseEntity<?> completeTest(@PathVariable("testId") Integer testId, @RequestBody List<String> userAnswers){
        try{
            Optional<Boolean> result = Optional.of(testService.completeTest(testId, userAnswers));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{testId}")
    public ResponseEntity<?> findTestById(@PathVariable("testId") Integer testId){
        try {
            Optional<Test> result = Optional.ofNullable(testService.findById(testId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}

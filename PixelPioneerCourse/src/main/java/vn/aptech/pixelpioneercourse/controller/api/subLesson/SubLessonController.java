package vn.aptech.pixelpioneercourse.controller.api.subLesson;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.SubLessonCreateDto;

import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.service.SubLessonService;
import vn.aptech.pixelpioneercourse.until.ControllerUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sub-lesson")
public class SubLessonController {

    private final SubLessonService subLessonService;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public SubLessonController(SubLessonService subLessonService, ObjectMapper objectMapper, Validator validator) {
        this.subLessonService = subLessonService;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<?> findSubLessonByLessonId(@PathVariable("lessonId") Integer lessonId) {
        try {
            Optional<List<SubLesson>> result = Optional.ofNullable(subLessonService.findAllSubLessonsByLessonId(lessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{subLessonId}")
    public ResponseEntity<?> findSubLessonById(@PathVariable("subLessonId") Integer subLessonId) {
        try {
            Optional<SubLesson> result = Optional.ofNullable(subLessonService.findById(subLessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{subLessonId}/status/complete")
    public ResponseEntity<?> completeSubLesson(@PathVariable("subLessonId") Integer subLessonId){
        try {
            Optional<SubLesson> result = Optional.ofNullable(subLessonService.completeSubLesson(subLessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{subLessonId}/update")
    public ResponseEntity<?> updateSubLesson(@PathVariable("subLessonId") Integer subLessonId,
                                             @RequestParam("image") MultipartFile image,
                                             @RequestPart("subLessonData") String subLessonData) {
        try {
            SubLessonCreateDto dto = objectMapper.readValue(subLessonData, SubLessonCreateDto.class);
            dto.setImage(image);
            BindingResult bindingResult = new BeanPropertyBindingResult(dto, "subLessonCreateDto");
            validator.validate(dto, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ControllerUtils.getErrorMessages(bindingResult));
            }
            Optional<SubLesson> result = Optional.ofNullable(subLessonService.update(subLessonId, dto));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing sub-lesson data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{subLessonId}/delete")
    public ResponseEntity<?> deleteSubLesson(@PathVariable("subLessonId") Integer subLessonId){
        try {
            Optional<Boolean> result = Optional.of(subLessonService.delete(subLessonId));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSubLesson(@RequestParam("image") MultipartFile image,
                                             @RequestPart("subLessonData") String subLessonData) {
        try {
            SubLessonCreateDto dto = objectMapper.readValue(subLessonData, SubLessonCreateDto.class);
            dto.setImage(image);
            BindingResult bindingResult = new BeanPropertyBindingResult(dto, "subLessonCreateDto");
            validator.validate(dto, bindingResult);
            if(bindingResult.hasErrors()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ControllerUtils.getErrorMessages(bindingResult));
            }
            Optional<SubLesson> result = Optional.ofNullable(subLessonService.save(dto));
            return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing sub-lesson data: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}

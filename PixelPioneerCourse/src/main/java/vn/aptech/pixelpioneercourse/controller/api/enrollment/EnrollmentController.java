package vn.aptech.pixelpioneercourse.controller.api.enrollment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.EnrollmentCreateDto;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }
    
    @GetMapping
    public ResponseEntity<List<Enrollment>> findAll() {
        return ResponseEntity.ok(enrollmentService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> findById(@PathVariable("id") Integer id) {
        Enrollment enrollment = enrollmentService.findById(id);
        return (enrollment == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(enrollment);
    }
    
    @PostMapping("/create")
    public ResponseEntity<Enrollment> createEnrollment(@Valid @RequestBody EnrollmentCreateDto enrollmentCreateDto) {
        Enrollment savedEnrollment = enrollmentService.createEnrollment(enrollmentCreateDto);
        return (savedEnrollment == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(savedEnrollment);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@Valid @PathVariable("id") Integer id, @RequestBody EnrollmentCreateDto enrollmentDetails) {
        Enrollment updatedEnrollment = enrollmentService.updateEnrollment(id, enrollmentDetails);
        return (updatedEnrollment == null) ? ResponseEntity.badRequest().build() : ResponseEntity.ok(updatedEnrollment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        Enrollment existingEnrollment = enrollmentService.findById(id);
        return (existingEnrollment == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok().build();
    }
    
}

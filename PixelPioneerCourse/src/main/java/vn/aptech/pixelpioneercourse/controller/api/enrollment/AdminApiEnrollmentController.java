package vn.aptech.pixelpioneercourse.controller.api.enrollment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/enrollments")
public class AdminApiEnrollmentController {

    private final EnrollmentService enrollmentService;

    public AdminApiEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<Enrollment> getAllEnrollments() {
        return enrollmentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(enrollmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
        return ResponseEntity.ok(enrollmentService.save(enrollment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Enrollment> updateEnrollment(@PathVariable("id") Integer id, @RequestBody Enrollment enrollment) {
        enrollment.setId(id);
        return ResponseEntity.ok(enrollmentService.save(enrollment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable("id") Integer id) {
        enrollmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    
}

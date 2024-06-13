package vn.aptech.pixelpioneercourse.controller.app.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.EnrollmentRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;

import java.util.List;

@Controller("adminEnrollmentController")
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listEnrollments(Model model) {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        model.addAttribute("enrollments", enrollments);
        return "app/admin_view/enrollment/index";
    }

    @GetMapping("/user/{userId}")
    public String listEnrollmentsByUser(@PathVariable @SessionAttribute("userId") Integer userId, Model model) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            model.addAttribute("error", "User not found");
            return "admin/enrollments";
        }
        List<Enrollment> enrollments = enrollmentRepository.findEnrollmentsByUserId(userId);
        model.addAttribute("enrollments", enrollments);
        return "admin/enrollments";
    }

    @PostMapping("/delete/{enrollmentId}")
    public String deleteEnrollment(@PathVariable Integer enrollmentId, Model model) {
        enrollmentRepository.deleteById(enrollmentId);
        return "redirect:/admin/enrollments";
    }

    @PostMapping("/update/{enrollmentId}")
    public String updateEnrollment(@PathVariable Integer enrollmentId, @RequestParam String status, Model model) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
        if (enrollment == null) {
            model.addAttribute("error", "Enrollment not found");
            return "admin/enrollments";
        }
        // Update enrollment status or other fields here
        // enrollment.setStatus(status); // Assuming there's a status field
        enrollmentRepository.save(enrollment);
        return "redirect:/admin/enrollments";
    }
}


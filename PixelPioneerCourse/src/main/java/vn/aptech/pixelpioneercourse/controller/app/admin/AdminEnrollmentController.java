package vn.aptech.pixelpioneercourse.controller.app.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;

@Controller("adminEnrollmentController")
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;

    public AdminEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("enrollments", enrollmentService.findAll());
        return "app/admin_view/enrollment/index";
    }

    @GetMapping("/details/{id}")
    public String enrollmentDetails(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("enrollment", enrollmentService.findById(id));
        return "app/admin_view/enrollment/enrollment-details";
    }

    @GetMapping("/new")
    public String newEnrollmentForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        return "admin/enrollments/new";
    }
}

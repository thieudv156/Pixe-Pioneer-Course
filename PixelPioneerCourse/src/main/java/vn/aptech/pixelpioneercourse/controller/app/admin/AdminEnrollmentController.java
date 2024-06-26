package vn.aptech.pixelpioneercourse.controller.app.admin;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.service.EmailService;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller("adminEnrollmentController")
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    private final EnrollmentService enrollmentService;
    private final EmailService emailService;

    public AdminEnrollmentController(EnrollmentService enrollmentService, EmailService emailService) {
        this.enrollmentService = enrollmentService;
        this.emailService = emailService;
    }

    @GetMapping
    public String index(Model model) {
        List<Enrollment> enrollments = enrollmentService.findAll();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Enrollment enrollment : enrollments) {
            String formattedPaymentDate = enrollment.getPaymentDate().toLocalDate().format(formatter);
            String formattedSubscriptionEndDate = enrollment.getSubscriptionEndDate().toLocalDate().format(formatter);

            enrollment.setFormattedPaymentDate(formattedPaymentDate);
            enrollment.setFormattedSubscriptionEndDate(formattedSubscriptionEndDate);

            boolean status = enrollment.isSubcriptionActive();
            enrollment.setSubscriptionStatus(status);
        }
        model.addAttribute("enrollments", enrollments);
        return "app/admin_view/enrollment/index";
    }

    @GetMapping("/details/{id}")
    public String enrollmentDetails(@PathVariable("id") Integer id, Model model) {
        Enrollment enrollment = enrollmentService.findById(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedPaymentDate = enrollment.getPaymentDate().toLocalDate().format(formatter);
        String formattedSubscriptionEndDate = enrollment.getSubscriptionEndDate().toLocalDate().format(formatter);

        enrollment.setFormattedPaymentDate(formattedPaymentDate);
        enrollment.setFormattedSubscriptionEndDate(formattedSubscriptionEndDate);

        model.addAttribute("enrollment", enrollment);
        return "app/admin_view/enrollment/enrollment-details";
    }

    @GetMapping("/delete/{id}")
    public String showDeleteConfirmation(@PathVariable("id") Integer id, Model model) {
        Enrollment enrollment = enrollmentService.findById(id);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedPaymentDate = enrollment.getPaymentDate().toLocalDate().format(formatter);
        String formattedSubscriptionEndDate = enrollment.getSubscriptionEndDate().toLocalDate().format(formatter);

        enrollment.setFormattedPaymentDate(formattedPaymentDate);
        enrollment.setFormattedSubscriptionEndDate(formattedSubscriptionEndDate);

        model.addAttribute("enrollment", enrollment);
        return "app/admin_view/enrollment/delete_enrollment";
    }

    @PostMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable("id") Integer id, @RequestParam("reason") String reason, RedirectAttributes redirectAttributes) {
        if (reason.isBlank()) {
            redirectAttributes.addFlashAttribute("error", "Reason for deletion cannot be blank");
            return "redirect:/admin/enrollments/delete/" + id;
        }
        Enrollment enrollment = enrollmentService.findById(id);
        // Send email to the user
        emailService.sendDeletionEmail(enrollment, reason);
        // Delete the enrollment
        enrollmentService.deleteById(id);
        return "redirect:/admin/enrollments";
    }

    @GetMapping("/export")
    public String exportToExcel(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate, HttpServletResponse response, RedirectAttributes redirectAttributes) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        if (start.isAfter(end)) {
            redirectAttributes.addFlashAttribute("error", "Start date must be before or equal to end date");
            return "redirect:/admin/enrollments";  // Redirect to the enrollment page or an appropriate page
        }

        List<Enrollment> enrollments = enrollmentService.findByDateRange(start, end);
        ByteArrayInputStream in = enrollmentService.exportEnrollmentsToExcel(enrollments);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=enrollments.xlsx");

        IOUtils.copy(in, response.getOutputStream());
        response.flushBuffer();
        return null;  // Since the response is handled directly, return null
    }


}

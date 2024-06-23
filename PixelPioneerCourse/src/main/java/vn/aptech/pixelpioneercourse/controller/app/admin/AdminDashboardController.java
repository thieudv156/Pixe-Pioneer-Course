package vn.aptech.pixelpioneercourse.controller.app.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.service.AdminDashboardService;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/app/admin")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;
    
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        double totalSales = adminDashboardService.getTotalSales();
        int userCount = adminDashboardService.getTotalActiveUsers().size();
        double earningsThisMonth = adminDashboardService.getEarningsThisMonth();
        Map<LocalDate, Double> dailySalesThisWeek = adminDashboardService.getDailySalesThisWeek();
        Map<LocalDate, Double> dailySalesLastWeek = adminDashboardService.getDailySalesLastWeek();
        List<Enrollment> enrollments = enrollmentService.get10LatestEnrollments();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Enrollment enrollment : enrollments) {
            String formattedPaymentDate = enrollment.getPaymentDate().toLocalDate().format(formatter);
            String formattedSubscriptionEndDate = enrollment.getSubscriptionEndDate().toLocalDate().format(formatter);

            enrollment.setFormattedPaymentDate(formattedPaymentDate);
            enrollment.setFormattedSubscriptionEndDate(formattedSubscriptionEndDate);

            boolean status = enrollment.isSubcriptionActive();
            enrollment.setSubscriptionStatus(status);
        }

        model.addAttribute("totalSales", totalSales);
        model.addAttribute("userCount", userCount);
        model.addAttribute("earningsThisMonth", earningsThisMonth);
        model.addAttribute("dailySalesThisWeek", dailySalesThisWeek);
        model.addAttribute("dailySalesLastWeek", dailySalesLastWeek);
        model.addAttribute("enrollments", enrollments);

        // Convert the map to a format that can be used in JavaScript
        model.addAttribute("dailySalesThisWeekValues", new ArrayList<>(dailySalesThisWeek.values()));
        model.addAttribute("dailySalesThisWeekLabels", dailySalesThisWeek.keySet().stream().map(LocalDate::toString).collect(Collectors.toList()));
        System.out.println(new ArrayList<>(dailySalesThisWeek.values()));
        System.out.println(dailySalesThisWeek.keySet().stream().map(LocalDate::toString).collect(Collectors.toList()));
        return "app/admin_view/dashboard/admin-dashboard";
    }
}

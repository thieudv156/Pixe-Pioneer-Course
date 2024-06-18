package vn.aptech.pixelpioneercourse.controller.app.enrollment;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

@Controller("enrollmentAppController")
@RequestMapping("app/enrollments")
@SessionAttributes("userId")
public class EnrollmentController {

    private final UserRepository userRepository;

    public EnrollmentController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @ModelAttribute("userId")
    public Integer setUserSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); //no principal
        User user = userRepository.findByUsername(username).orElse(null);
        return (user != null) ? user.getId() : null;
    }
    
    @GetMapping
    public String showEnrollments() {
        return "app/enrollment/subscription";
    }

    @GetMapping("/purchase-details")
    public String showPurchaseSummary(@RequestParam String subscriptionType, Model model) {
        double price = getPrice(subscriptionType);
        model.addAttribute("subscriptionType", subscriptionType);
        model.addAttribute("price", price);
        return "app/enrollment/purchase-details";
    }


    @GetMapping("/payment-success")
    public String showPaymentSuccess(Model model) {
        return "app/enrollment/payment-success";
    }

    private double getPrice(String subscriptionType) {
        return switch (subscriptionType) {
            case "MONTHLY" -> 9.0;
            case "YEARLY" -> 99.0;
            case "UNLIMITED" -> 999.0;
            default -> throw new IllegalArgumentException("Invalid subscription type");
        };
    }
}

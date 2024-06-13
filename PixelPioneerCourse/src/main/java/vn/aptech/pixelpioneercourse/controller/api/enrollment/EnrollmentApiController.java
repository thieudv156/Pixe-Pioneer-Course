package vn.aptech.pixelpioneercourse.controller.api.enrollment;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.entities.SubscriptionType;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;
import vn.aptech.pixelpioneercourse.service.PaymentServiceImpl;


@RestController("enrollmentApiController")
@RequestMapping("/api/enrollments")
public class EnrollmentApiController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private PaymentServiceImpl paymentService;

    @PostMapping("/process-payment")
    public ResponseEntity<?> processPayment(HttpSession session,
                                            @RequestParam String subscriptionType,
                                            @RequestParam double price,
                                            @RequestParam PaymentMethod paymentMethod) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        try {
            if (paymentMethod == PaymentMethod.PAYPAL) {
                String approvalUrl = paymentService.createPayPalPayment(price, "USD", "paypal", "sale", "Course subscription", session, subscriptionType);
                if (approvalUrl != null) {
                    return ResponseEntity.ok(approvalUrl);
                } else {
                    throw new RuntimeException("Error creating PayPal payment");
                }
            } else if (paymentMethod == PaymentMethod.CREDIT_CARD) {
                boolean paymentSuccessful = paymentService.processCreditCardPayment("mockCardNumber", "12/2025", "123", price, "USD"); // Mock card details
                if (paymentSuccessful) {
                    enrollmentService.enrollUser(userId, SubscriptionType.valueOf(subscriptionType), paymentMethod);
                    return ResponseEntity.ok("Payment successful");
                } else {
                    throw new RuntimeException("Credit card payment failed");
                }
            }

            throw new RuntimeException("Invalid payment method");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Subscription failed: " + e.getMessage());
        }
    }
}

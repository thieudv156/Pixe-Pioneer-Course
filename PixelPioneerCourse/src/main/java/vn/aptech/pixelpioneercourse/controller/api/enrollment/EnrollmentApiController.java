package vn.aptech.pixelpioneercourse.controller.api.enrollment;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.aptech.pixelpioneercourse.dto.PaymentRequest;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.entities.SubscriptionType;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;
import vn.aptech.pixelpioneercourse.service.PaymentService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enrollments")
@Validated
public class EnrollmentApiController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process-payment")
    public ResponseEntity<?> processPayment(@SessionAttribute("userId") Integer userId, @Valid @RequestBody PaymentRequest paymentRequest, HttpSession session) {
        if (userId == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        try {
            if (paymentRequest.getPaymentMethod() == PaymentMethod.PAYPAL) {
                String approvalUrl = paymentService.createPayPalPayment(paymentRequest.getPrice(), "USD", "paypal", "sale", "Course subscription", session, paymentRequest.getSubscriptionType());
                if (approvalUrl != null) {
                    return ResponseEntity.ok(approvalUrl);
                } else {
                    throw new RuntimeException("PayPal payment creation failed");
                }
            } else if (paymentRequest.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {
                boolean paymentSuccessful = paymentService.processCreditCardPayment(paymentRequest.getCardNumber(), paymentRequest.getExpiration(), paymentRequest.getCvv(), paymentRequest.getPrice(), "USD");
                if (paymentSuccessful) {
                    enrollmentService.enrollUser(userId, SubscriptionType.valueOf(paymentRequest.getSubscriptionType()), paymentRequest.getPaymentMethod());
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

    @GetMapping("/check-enrollment")
    public ResponseEntity<?> checkEnrollment(@SessionAttribute("userId") Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        boolean isEnrolledAndPaid = enrollmentService.isUserEnrolledAndPaid(userId);
        return ResponseEntity.ok(isEnrolledAndPaid);
    }

    @GetMapping("/check-enrollments")
    public ResponseEntity<?> checkEnrollments(@RequestParam("userId") String userId) {
        if (userId == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }
        int parsedUserId = Integer.parseInt(userId);
        boolean isEnrolledAndPaid = enrollmentService.isUserEnrolledAndPaid(parsedUserId);
        if (isEnrolledAndPaid) {
            return ResponseEntity.ok(isEnrolledAndPaid);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(isEnrolledAndPaid);
        }
    }

    @GetMapping("/get-subscription")
    public ResponseEntity<?> getSubscription(@RequestParam("userId") String userId) {
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not authenticated");
        }
        int parsedUID = Integer.parseInt(userId);
        try {
            Enrollment enrollInfo = enrollmentService.findByUserId(parsedUID);
            List<Map<String, String>> subscriptionInfo = new ArrayList<>(); // Initialize the list
            subscriptionInfo.add(Map.of("user_name", enrollInfo.getUser().getFullName()));
            subscriptionInfo.add(Map.of("subscription_state", enrollInfo.getSubscriptionStatus().toString()));
            subscriptionInfo.add(Map.of("subscription_package_name", enrollInfo.getSubscriptionType().toString()));
            subscriptionInfo.add(Map.of("validity", Period.between(LocalDateTime.now().toLocalDate(), enrollInfo.getSubscriptionEndDate().toLocalDate()).toString()));

            ObjectMapper objectMapper = new ObjectMapper(); // Create an ObjectMapper instance
            String json = objectMapper.writeValueAsString(subscriptionInfo); // Convert subscriptionInfo to JSON

            return ResponseEntity.status(HttpStatus.OK).body(json); // Return the JSON string
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

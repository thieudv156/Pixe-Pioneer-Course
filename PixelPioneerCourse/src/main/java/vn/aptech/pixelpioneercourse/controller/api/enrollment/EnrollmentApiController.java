package vn.aptech.pixelpioneercourse.controller.api.enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.PaymentRequest;
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
    public ResponseEntity<?> checkEnrollments(@RequestParam("userId") Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        boolean isEnrolledAndPaid = enrollmentService.isUserEnrolledAndPaid(userId);
        if (isEnrolledAndPaid) {
        	return ResponseEntity.ok(isEnrolledAndPaid);
        } else {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(isEnrolledAndPaid);
        }
    }
}

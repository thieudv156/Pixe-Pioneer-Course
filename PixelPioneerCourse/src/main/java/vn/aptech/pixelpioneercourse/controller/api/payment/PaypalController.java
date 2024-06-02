package vn.aptech.pixelpioneercourse.controller.api.payment;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.service.PayPalService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
public class PaypalController {
    @Autowired
    private PayPalService payPalService;

    @Value("${paypal.success.url}")
    private String successUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> pay(@RequestParam double amount) {
        Map<String, String> response = new HashMap<>();
        try {
            Payment payment = payPalService.createPayment(
                    amount,
                    "USD",
                    "paypal",
                    "sale",
                    "Course payment",
                    cancelUrl,
                    successUrl);
            for (Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    response.put("approval_url", link.getHref());
                    return ResponseEntity.ok(response);
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            response.put("error", "Payment creation failed");
            return ResponseEntity.status(500).body(response);
        }
        response.put("error", "Payment creation failed");
        return ResponseEntity.status(500).body(response);
    }

    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        Map<String, String> response = new HashMap<>();
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                response.put("status", "success");
                response.put("paymentId", payment.getId());
                return ResponseEntity.ok(response);
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            response.put("error", "Payment execution failed");
            return ResponseEntity.status(500).body(response);
        }
        response.put("error", "Payment not approved");
        return ResponseEntity.status(500).body(response);
    }

    @GetMapping("/cancel")
    public ResponseEntity<Map<String, String>> cancel() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "cancelled");
        return ResponseEntity.ok(response);
    }
}

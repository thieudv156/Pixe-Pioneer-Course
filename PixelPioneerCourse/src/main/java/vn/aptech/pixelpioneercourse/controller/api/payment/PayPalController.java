package vn.aptech.pixelpioneercourse.controller.api.payment;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Value;
import vn.aptech.pixelpioneercourse.dto.CreditCardDto;
import vn.aptech.pixelpioneercourse.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @Value("${paypal.success.url}")
    private String successUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> payWithPayPal(@RequestParam double amount) {
        Map<String, String> response = new HashMap<>();
        try {
            Payment payment = payPalService.createPayPalPayment(
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

    @PostMapping("/pay-with-card")
    public ResponseEntity<Map<String, String>> payWithCard(@RequestBody CreditCardDto creditCardDto) {
        Map<String, String> response = new HashMap<>();
        try {
            Payment payment = payPalService.createCreditCardPayment(
                    10.00,
                    "USD",
                    "credit_card",
                    "sale",
                    "Course payment",
                    creditCardDto.toCreditCard());
            if (payment.getState().equals("approved")) {
                response.put("status", "success");
                response.put("paymentId", payment.getId());
                return ResponseEntity.ok(response);
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            response.put("error", "Payment creation failed");
            return ResponseEntity.status(500).body(response);
        }
        response.put("error", "Payment not approved");
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
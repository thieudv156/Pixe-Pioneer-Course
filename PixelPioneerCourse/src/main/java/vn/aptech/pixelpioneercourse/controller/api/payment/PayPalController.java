package vn.aptech.pixelpioneercourse.controller.api.payment;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Links;
import com.paypal.base.rest.PayPalRESTException;
import vn.aptech.pixelpioneercourse.dto.PaymentRequestDto;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.service.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody PaymentRequestDto paymentRequestDto) {
        Map<String, String> response = new HashMap<>();
        try {
            Payment payment = payPalService.createPayment(paymentRequestDto, cancelUrl, successUrl);
            if (paymentRequestDto.getPaymentMethod() == PaymentMethod.PAYPAL) {
                for (Links link : payment.getLinks()) {
                    if ("approval_url".equals(link.getRel())) {
                        response.put("approval_url", link.getHref());
                        return ResponseEntity.ok(response);
                    }
                }
                response.put("error", "Approval URL not found");
            } else if ("approved".equals(payment.getState())) {
                response.put("status", "success");
                response.put("paymentId", payment.getId());
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Payment not approved");
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            response.put("error", "Payment creation failed");
            return ResponseEntity.status(500).body(response);
        }
        return ResponseEntity.status(500).body(response);
    }

    @GetMapping("/success")
    public ResponseEntity<Map<String, String>> success(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        Map<String, String> response = new HashMap<>();
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            if ("approved".equals(payment.getState())) {
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
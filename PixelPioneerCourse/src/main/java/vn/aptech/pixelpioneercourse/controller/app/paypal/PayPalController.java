package vn.aptech.pixelpioneercourse.controller.app.paypal;


import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.entities.SubscriptionType;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;

@Controller("paypalController")
public class PayPalController {

    @Autowired
    private APIContext apiContext;

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/paypal/cancel")
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping("/paypal/success")
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpSession session, Model model) {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        try {
            Payment executedPayment = payment.execute(apiContext, paymentExecution);
            if (executedPayment.getState().equals("approved")) {
                Integer userId = (Integer) session.getAttribute("userId");
                String subscriptionType = (String) session.getAttribute("subscriptionType");

                if (userId != null && subscriptionType != null) {
                    enrollmentService.enrollUser(userId, SubscriptionType.valueOf(subscriptionType), PaymentMethod.PAYPAL);
                }

                model.addAttribute("payment", executedPayment);
                return "app/enrollment/payment-success";
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return "redirect:/app/course";
    }
}

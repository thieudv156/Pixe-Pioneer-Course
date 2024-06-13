package vn.aptech.pixelpioneercourse.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Value("${paypal.success.url}")
    private String successUrl;

    @Value("${paypal.cancel.url}")
    private String cancelUrl;

    @Autowired
    private APIContext apiContext;

    // Process PayPal Payment
    public String createPayPalPayment(double total, String currency, String method, String intent, String description, HttpSession session, String subscriptionType) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(List.of(transaction));

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment = payment.create(apiContext);
        for (Links links : createdPayment.getLinks()) {
            if (links.getRel().equals("approval_url")) {
                session.setAttribute("subscriptionType", subscriptionType);
                return links.getHref();
            }
        }
        return null;
    }

    // Process Credit Card Payment
    public boolean processCreditCardPayment(String cardNumber, String expiration, String cvv, double total, String currency) {
        // Mock implementation of credit card payment
        // In a real application, use a service like Stripe or Braintree
        // Here, we assume the payment is always successful for demonstration purposes
        return true;
    }
}

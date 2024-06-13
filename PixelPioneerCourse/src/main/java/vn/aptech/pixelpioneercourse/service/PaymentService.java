package vn.aptech.pixelpioneercourse.service;

import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpSession;

public interface PaymentService {
    String createPayPalPayment(double total, String currency, String method, String intent, String description, HttpSession session, String subscriptionType) throws PayPalRESTException;
    boolean processCreditCardPayment(String cardNumber, String expiration, String cvv, double total, String currency);
}

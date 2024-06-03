package vn.aptech.pixelpioneercourse.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.CreditCardDto;
import vn.aptech.pixelpioneercourse.dto.PaymentRequestDto;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private APIContext apiContext;

    public Payment createPayment(PaymentRequestDto paymentRequestDto, String cancelUrl, String successUrl) throws PayPalRESTException {
        if (paymentRequestDto.getPaymentMethod() == PaymentMethod.PAYPAL) {
            return createPayPalPayment(paymentRequestDto.getAmount(), "USD", "paypal", "sale", "Course payment", cancelUrl, successUrl);
        } else if (paymentRequestDto.getPaymentMethod() == PaymentMethod.CREDIT_CARD && paymentRequestDto.getCreditCard() != null) {
            return createCreditCardPayment(paymentRequestDto.getAmount(), "USD", "sale", "Course payment", paymentRequestDto.getCreditCard());
        } else {
            throw new IllegalArgumentException("Invalid payment method or missing credit card information");
        }
    }

    private Payment createPayPalPayment(Double total, String currency, String method, String intent, String description, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    private Payment createCreditCardPayment(Double total, String currency, String intent, String description, CreditCardDto creditCardDto) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        FundingInstrument fundingInstrument = new FundingInstrument();
        fundingInstrument.setCreditCard(creditCardDto.toCreditCard());

        Payer payer = new Payer();
        payer.setPaymentMethod("credit_card");
        payer.setFundingInstruments(List.of(fundingInstrument));

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecution);
    }
}

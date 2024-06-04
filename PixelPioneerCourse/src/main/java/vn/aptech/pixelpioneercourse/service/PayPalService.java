package vn.aptech.pixelpioneercourse.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import vn.aptech.pixelpioneercourse.dto.PaymentRequestDto;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    @Autowired
    private APIContext apiContext;

    public Payment createPayment(PaymentRequestDto paymentRequestDto, String cancelUrl, String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", paymentRequestDto.getAmount()));

        Transaction transaction = new Transaction();
        transaction.setDescription("Course Payment");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        if (paymentRequestDto.getPaymentMethod() == PaymentMethod.PAYPAL) {
            payer.setPaymentMethod("paypal");
        } else if (paymentRequestDto.getPaymentMethod() == PaymentMethod.CREDIT_CARD) {
            payer.setPaymentMethod("credit_card");
            CreditCard creditCard = new CreditCard()
                .setNumber(paymentRequestDto.getCreditCard().getNumber())
                .setType(paymentRequestDto.getCreditCard().getType())
                .setExpireMonth(Integer.parseInt(paymentRequestDto.getCreditCard().getExpireMonth()))
                .setExpireYear(Integer.parseInt(paymentRequestDto.getCreditCard().getExpireYear()))
                .setCvv2(paymentRequestDto.getCreditCard().getCvv2())
                .setFirstName(paymentRequestDto.getCreditCard().getFirstName())
                .setLastName(paymentRequestDto.getCreditCard().getLastName());
            FundingInstrument fundingInstrument = new FundingInstrument();
            fundingInstrument.setCreditCard(creditCard);
            List<FundingInstrument> fundingInstrumentList = new ArrayList<>();
            fundingInstrumentList.add(fundingInstrument);
            payer.setFundingInstruments(fundingInstrumentList);
        }

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

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

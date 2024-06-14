package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;


@Data
public class PaymentRequest {
    @NotNull
    private Integer userId;

    @NotBlank
    private String subscriptionType;

    @NotNull
    @Positive
    private Double price;

    @NotNull
    private PaymentMethod paymentMethod;

    @Pattern(regexp = "^[0-9]{16}$", message = "Invalid card number. It should be 16 digits.")
    private String cardNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Invalid expiration date. Format should be MM/YY.")
    private String expiration;

    @Pattern(regexp = "^[0-9]{3,4}$", message = "Invalid CVV. It should be 3 or 4 digits.")
    private String cvv;
}

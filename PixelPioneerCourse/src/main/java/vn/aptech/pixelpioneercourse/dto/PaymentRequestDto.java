package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {

    @NotNull(message = "Amount is mandatory")
    private Double amount;

    @NotNull(message = "Payment method is mandatory")
    private PaymentMethod paymentMethod;

    private CreditCardDto creditCard;
}
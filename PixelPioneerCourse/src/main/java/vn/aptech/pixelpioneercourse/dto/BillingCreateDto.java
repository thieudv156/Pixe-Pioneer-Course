package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingCreateDto {

    @NotNull(message = "Payment method is mandatory")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Bill date is mandatory")
    private LocalDateTime billDate;

    @NotNull(message = "Total is mandatory")
    private Double total;

    @NotNull(message = "Payment status is mandatory")
    private Boolean paymentStatus;
}

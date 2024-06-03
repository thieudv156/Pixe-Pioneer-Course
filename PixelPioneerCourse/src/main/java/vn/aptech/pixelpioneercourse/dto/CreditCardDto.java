package vn.aptech.pixelpioneercourse.dto;

import com.paypal.api.payments.CreditCard;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardDto {

    @NotBlank(message = "Credit card number is mandatory")
    private String number;

    @NotBlank(message = "Credit card type is mandatory")
    private String type;

    @NotBlank(message = "Credit card expiration month is mandatory")
    private String expireMonth;

    @NotBlank(message = "Credit card expiration year is mandatory")
    private String expireYear;

    @NotBlank(message = "Credit card CVV2 is mandatory")
    private String cvv2;

    @NotBlank(message = "Credit card first name is mandatory")
    private String firstName;

    @NotBlank(message = "Credit card last name is mandatory")
    private String lastName;

    public CreditCard toCreditCard() {
        CreditCard creditCard = new CreditCard();
        creditCard.setNumber(this.number);
        creditCard.setType(this.type);
        creditCard.setExpireMonth(Integer.parseInt(this.expireMonth));
        creditCard.setExpireYear(Integer.parseInt(this.expireYear));
        creditCard.setCvv2(this.cvv2);
        creditCard.setFirstName(this.firstName);
        creditCard.setLastName(this.lastName);
        return creditCard;
    }
}

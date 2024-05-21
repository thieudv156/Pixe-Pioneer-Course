package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCreateDto {

    @NotNull(message = "Course ID is mandatory")
    private Integer courseId;

    @NotNull(message = "User ID is mandatory")
    private Integer userId;

    @NotNull(message = "Payment ID is mandatory")
    private Integer paymentId;
}

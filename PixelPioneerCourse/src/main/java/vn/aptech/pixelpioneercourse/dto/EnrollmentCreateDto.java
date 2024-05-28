package vn.aptech.pixelpioneercourse.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCreateDto {

    @NotNull(message = "Course ID is required")
    private Integer courseId;

    @NotNull(message = "User ID is required")
    private Integer userId;

    @Min(value = 0, message = "Progress must be at least 0")
    @Max(value = 100, message = "Progress must be at most 100")
    private int progress = 0;

    @NotNull(message = "Enrollment date is required")
    private LocalDateTime enrolledAt;

    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}

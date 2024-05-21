package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDto {

    @NotNull(message = "Course ID is mandatory")
    private Integer courseId;

    @NotNull(message = "User ID is mandatory")
    private Integer userId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Comment is mandatory")
    @Size(max = 1000, message = "Comment must be less than 1000 characters")
    private String comment;
}

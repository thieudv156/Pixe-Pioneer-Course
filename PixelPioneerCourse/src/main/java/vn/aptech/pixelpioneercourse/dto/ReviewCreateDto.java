package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDto {

    @NotNull(message = "Course ID is required")
    private Integer courseId;

    @NotNull(message = "Student ID is required")
    private Integer userId;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int rating;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Created at date is required")
    private LocalDateTime createdAt;
}

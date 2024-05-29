package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateDto {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotNull(message = "Category ID is mandatory")
    private int categoryId;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @NotNull(message = "Price is mandatory")
    @PositiveOrZero(message = "Price must be zero or positive")
    private double price;

    @NotNull(message = "Instructor ID is mandatory")
    private int instructorId;

    private String image;
}

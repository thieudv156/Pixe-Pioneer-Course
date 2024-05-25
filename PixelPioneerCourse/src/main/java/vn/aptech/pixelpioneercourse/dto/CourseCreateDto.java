package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CourseCreateDto {

    private Integer id;

    @NotBlank(message = "Title is mandatory")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotNull(message = "Category ID is mandatory")
    private Integer categoryId;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @Positive(message = "Price must be positive")
    private double price;

    @NotNull(message = "Instructor ID is mandatory")
    private Integer instructorId;
}

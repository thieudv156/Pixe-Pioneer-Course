package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.awt.geom.QuadCurve2D;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseCreateDto {
    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotNull(message = "Category ID is mandatory")
    private Integer categoryId;

    @NotBlank(message = "Description is mandatory")
    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @NotNull(message = "Price is mandatory")
    @Min( value = 1,message = "Price must be 1 or positive")
    private Double price;

    private Integer instructorId;

}
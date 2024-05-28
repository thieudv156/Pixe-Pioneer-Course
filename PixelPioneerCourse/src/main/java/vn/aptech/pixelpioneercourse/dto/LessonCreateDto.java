package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonCreateDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Course ID is required")
    private Integer courseId;

    @NotBlank(message = "Image is required")
    private String image;

    @NotNull(message = "Created at date is required")
    private LocalDateTime createdAt;
}

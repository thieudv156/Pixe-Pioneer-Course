package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonCreateDto {

    @NotBlank(message = "Title is mandatory")
    private String title;

    @NotNull(message = "Course ID is mandatory")
    private Integer courseId;

    @Positive(message = "Duration must be positive")
    private int duration;

    @NotNull(message = "Created at date is mandatory")
    private LocalDateTime createdAt;
}

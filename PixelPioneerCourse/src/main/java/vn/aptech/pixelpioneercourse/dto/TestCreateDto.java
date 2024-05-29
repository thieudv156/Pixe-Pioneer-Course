package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestCreateDto {

    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @NotNull(message = "Lesson ID is required")
    private Integer lessonId;

    @Min(value = 0, message = "Score must be at least 0")
    @Max(value = 100, message = "Score must be at most 100")
    private int score;
}

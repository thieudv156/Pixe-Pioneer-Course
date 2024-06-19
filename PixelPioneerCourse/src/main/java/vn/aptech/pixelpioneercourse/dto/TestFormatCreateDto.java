package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestFormatCreateDto {
    private Integer courseId;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @NotNull(message = "Total number of questions cannot be null")
    @Min(value = 1, message = "Total number of questions must be at least 1")
    private Integer totalQuestion;

    @NotNull(message = "Passing score cannot be null")
    @Min(value = 1, message = "Passing score must be at least 1")
    private Integer passingScore;
}
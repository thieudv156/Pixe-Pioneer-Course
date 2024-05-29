package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizzCreateDto {

    @NotBlank(message = "Question is required")
    private String question;

    @NotNull(message = "Lesson ID is mandatory")
    private Integer lessonId;

    @NotBlank(message = "Correct answer is required")
    private String correctAnswer;

    @NotBlank(message = "Option 1 is required")
    private String wrongAnswer1;

    @NotBlank(message = "Option 2 is required")
    private String wrongAnswer2;

    @NotBlank(message = "Option 3 is required")
    private String wrongAnswer3;
}

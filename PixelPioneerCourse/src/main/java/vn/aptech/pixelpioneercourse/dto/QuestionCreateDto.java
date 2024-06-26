package vn.aptech.pixelpioneercourse.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionCreateDto {
    private Integer courseId;

    private Integer id;

    @NotBlank(message = "Question cannot be blank")
    private String question;

    @NotBlank(message = "Correct answer cannot be blank")
    private String correctAnswer;

    @NotBlank(message = "Wrong answer 1 cannot be blank")
    private String wrongAnswer1;

    @NotBlank(message = "Wrong answer 2 cannot be blank")
    private String wrongAnswer2;

    @NotBlank(message = "Wrong answer 3 cannot be blank")
    private String wrongAnswer3;
}

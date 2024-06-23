package vn.aptech.pixelpioneercourse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {
    private Integer id;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answerChoice=null;
}

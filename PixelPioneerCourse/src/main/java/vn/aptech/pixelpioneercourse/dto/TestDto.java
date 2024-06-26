package vn.aptech.pixelpioneercourse.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.entities.TestFormat;
import vn.aptech.pixelpioneercourse.entities.User;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestDto {
    private Integer testFormatId;
    private User user;
    private Integer duration;
    private List<QuestionDto> questions;
}

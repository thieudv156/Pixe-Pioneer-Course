package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.QuestionCreateDto;
import vn.aptech.pixelpioneercourse.dto.QuestionDto;
import vn.aptech.pixelpioneercourse.entities.Question;

import java.util.List;

public interface QuestionService {

    Question save(QuestionCreateDto dto);
    Question findById(Integer id);
    boolean deleteById(Integer id);
    Question update(QuestionCreateDto dto, Integer id);
    List<Question> findByCourseId(Integer courseId);
    List<QuestionDto> mapRandomAnswer(Integer courseId, Integer totalQuestion);
}

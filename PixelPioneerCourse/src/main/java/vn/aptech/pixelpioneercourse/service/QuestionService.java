package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import vn.aptech.pixelpioneercourse.dto.QuestionCreateDto;
import vn.aptech.pixelpioneercourse.dto.QuestionView;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.repository.QuestionRepository;

import java.util.List;

public interface QuestionService {

    Question save(QuestionCreateDto dto);
    Question findById(Integer id);
    boolean deleteById(Integer id);
    Question update(QuestionCreateDto dto, Integer id);
    List<QuestionView> toQuestionViews(List<Question> questions);
}

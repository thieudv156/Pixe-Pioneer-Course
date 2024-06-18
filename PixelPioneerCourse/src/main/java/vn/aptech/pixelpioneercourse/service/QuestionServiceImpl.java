package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.QuestionCreateDto;
import vn.aptech.pixelpioneercourse.dto.QuestionView;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.repository.QuestionRepository;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService{
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final CourseService courseService;

    public QuestionServiceImpl(ModelMapper modelMapper, QuestionRepository questionRepository, CourseService courseService) {
        this.modelMapper = modelMapper;
        this.questionRepository = questionRepository;
        this.courseService = courseService;
    }

    public Question toQuestion(QuestionCreateDto dto){return modelMapper.map(dto, Question.class);}

    public Question save(QuestionCreateDto dto)
    {
        Course course = courseService.findById(dto.getCourseId());
        if(course == null)
        {
            throw new IllegalArgumentException("Course not found: "+dto.getCourseId());
        }
        Question question = toQuestion(dto);
        question.setCourse(course);
        return questionRepository.save(question);
    }

    public Question findById(Integer id)
    {
        return questionRepository.findById(id).orElse(null);
    }

    public boolean deleteById(Integer id)
    {
        try{
            questionRepository.deleteById(id);
            return true;
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Question not found: "+id);
        }
    }

    public Question update(QuestionCreateDto dto, Integer id)
    {
        Question question = findById(id);
        if(question == null)
        {
            throw new IllegalArgumentException("Question not found: "+id);
        }
        question.setCorrectAnswer(dto.getCorrectAnswer());
        question.setWrongAnswer1(dto.getWrongAnswer1());
        question.setWrongAnswer2(dto.getWrongAnswer2());
        question.setWrongAnswer3(dto.getWrongAnswer3());
        return questionRepository.save(question);
    }

    public List<QuestionView> toQuestionViews(List<Question> questions)
    {
        return questions.stream().map(question -> modelMapper.map(question, QuestionView.class)).toList();
    }

    public List<Question> findByCourseId(Integer courseId)
    {
        return questionRepository.findByCourseId(courseId);
    }
}

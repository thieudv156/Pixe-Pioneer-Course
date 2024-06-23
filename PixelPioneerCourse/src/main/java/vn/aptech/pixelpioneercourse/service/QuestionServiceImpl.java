package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.QuestionCreateDto;
import vn.aptech.pixelpioneercourse.dto.QuestionDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.entities.TestFormat;
import vn.aptech.pixelpioneercourse.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService{
    private final ModelMapper modelMapper;
    private final QuestionRepository questionRepository;
    private final CourseService courseService;
    private final TestFormatService testFormatService;

    public QuestionServiceImpl(ModelMapper modelMapper, QuestionRepository questionRepository, CourseService courseService, TestFormatService testFormatService) {
        this.modelMapper = modelMapper;
        this.questionRepository = questionRepository;
        this.courseService = courseService;
        this.testFormatService = testFormatService;
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

    public List<Question> findByCourseId(Integer courseId)
    {
        return questionRepository.findByCourseId(courseId);
    }

    public List<QuestionDto> mapRandomAnswer(Integer courseId, Integer totalQuestion){
        List<Question> questions = findByCourseId(courseId);
        Collections.shuffle(questions);
        List<Question> randomQuestions = questions.subList(0, Math.min(questions.size(), totalQuestion));
        return randomQuestions.stream().map(this::mapToDtoWithRandomAnswers).collect(Collectors.toList());
    }

    private QuestionDto mapToDtoWithRandomAnswers(Question question) {
        QuestionDto dto = new QuestionDto();
        dto.setId(question.getId());
        dto.setQuestion(question.getQuestion());

        List<String> answers = new ArrayList<>();
        answers.add(question.getCorrectAnswer());
        answers.add(question.getWrongAnswer1());
        answers.add(question.getWrongAnswer2());
        answers.add(question.getWrongAnswer3());

        Collections.shuffle(answers);

        dto.setAnswer1(answers.get(0));
        dto.setAnswer2(answers.get(1));
        dto.setAnswer3(answers.get(2));
        dto.setAnswer4(answers.get(3));
        System.out.println(dto);
        return dto;
    }

    public double checkAnswers(List<QuestionDto> questions, Integer formatId)
    {
        TestFormat testFormat = testFormatService.findById(formatId);
        if(formatId==null)
        {
            throw new IllegalArgumentException("TestFormat not found");
        }
        if(questions.size()!=testFormat.getTotalQuestion())
        {
            throw new IllegalArgumentException("Total question do not match with the amount of question!");
        }
        Double scorePerQuestion = (double) (testFormat.getTotalQuestion()/100);
        Double totalScore = 0.0;
        for(QuestionDto questionDto: questions)
        {
            if(checkAnswer(questionDto)){
                totalScore += scorePerQuestion;
            }
        }
        return totalScore;
    }

    public boolean checkAnswer(QuestionDto questionDto)
    {
        Question question = findById(questionDto.getId());
        return questionDto.equals(question.getCorrectAnswer());
    }
}

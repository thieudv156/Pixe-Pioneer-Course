package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.QuestionDto;
import vn.aptech.pixelpioneercourse.dto.TestDto;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.entities.Test;
import vn.aptech.pixelpioneercourse.entities.TestFormat;
import vn.aptech.pixelpioneercourse.repository.CourseCompleteRepository;
import vn.aptech.pixelpioneercourse.repository.TestRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService{
    private final TestRepository testRepository;
    private final ModelMapper modelMapper;
    private final TestFormatService testFormatService;
    private final UserService userService;
    private final QuestionService questionService;
    private final CourseCompleteService courseCompleteService;
    private final CourseCompleteRepository courseCompleteRepository;

    public TestServiceImpl(TestRepository testRepository, ModelMapper modelMapper, TestFormatService testFormatService, UserService userService, QuestionService questionService, CourseService courseService, CourseCompleteService courseCompleteService, CourseCompleteRepository courseCompleteRepository) {
        this.testRepository = testRepository;
        this.modelMapper = modelMapper;
        this.testFormatService = testFormatService;
        this.userService = userService;
        this.questionService = questionService;
        this.courseCompleteService = courseCompleteService;
        this.courseCompleteRepository = courseCompleteRepository;
    }

    public TestDto createTestDto(Integer testFormatId, Integer userId){
        TestDto test = new TestDto();
        test.setTestFormat(testFormatService.findById(testFormatId));
        test.setUser(userService.findById(userId));
        test.setDuration(testFormatService.findById(testFormatId).getDuration());
        List<QuestionDto> randomQuestions = questionService.mapRandomAnswer(testFormatService.findById(testFormatId).getCourse().getId(), testFormatService.findById(testFormatId).getTotalQuestion());
        test.setQuestions(randomQuestions);
        return test;
    }

    public Test submitTest(TestDto testDto, Integer userId)
    {
        List<QuestionDto> questionDtos = testDto.getQuestions();
        Double score = questionService.checkAnswers(questionDtos,testDto.getTestFormat().getId());
        Test test = new Test();
        test.setScore(score);
        test.setTestFormat(testDto.getTestFormat());
        test.setUser(userService.findById(userId));
        testRepository.save(test);
        if(test.getScore() >= testDto.getTestFormat().getPassingScore())
        {
            courseCompleteService.create(
                    userService.findById(userId),
                    testDto.getTestFormat().getCourse()
            );
        }
        return test;
    }
}

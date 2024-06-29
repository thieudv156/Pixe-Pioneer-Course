package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.QuestionDto;
import vn.aptech.pixelpioneercourse.dto.TestDto;
import vn.aptech.pixelpioneercourse.entities.Test;
import vn.aptech.pixelpioneercourse.entities.TestFormat;
import vn.aptech.pixelpioneercourse.repository.TestRepository;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final ModelMapper modelMapper;
    private final TestFormatService testFormatService;
    private final UserService userService;
    private final QuestionService questionService;
    private final CourseCompleteService courseCompleteService;
    private final ProgressService progressService;

    public TestServiceImpl(TestRepository testRepository, ModelMapper modelMapper, @Lazy TestFormatService testFormatService, UserService userService, @Lazy QuestionService questionService, CourseCompleteService courseCompleteService, CourseService courseService, ProgressService progressService) {
        this.testRepository = testRepository;
        this.modelMapper = modelMapper;
        this.testFormatService = testFormatService;
        this.userService = userService;
        this.questionService = questionService;
        this.courseCompleteService = courseCompleteService;
        this.progressService = progressService;
    }

    public TestDto createTestDto(Integer testFormatId, Integer userId) {
        try {
            if(progressService.getCurrentProgressByCourseId(testFormatService.findById(testFormatId).getCourse().getId(),userId)!=100)
            {
                throw new RuntimeException("You must have 100% progress before doing the final test!");
            }
            TestDto test = new TestDto();
            test.setTestFormatId(testFormatId);
            test.setUser(userService.findById(userId));
            test.setDuration(testFormatService.findById(testFormatId).getDuration());
            List<QuestionDto> randomQuestions = questionService.mapRandomAnswer(testFormatService.findById(testFormatId).getCourse().getId(), testFormatService.findById(testFormatId).getTotalQuestion());
            test.setQuestions(randomQuestions);
            System.out.println(test);
            return test;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Test submitTest(TestDto testDto, Integer userId) {
        List<QuestionDto> questionDtos = testDto.getQuestions();
        Double score = questionService.checkAnswers(questionDtos, testDto.getTestFormatId());
        System.out.println(score);
        TestFormat testFormat = testFormatService.findById(testDto.getTestFormatId());
        Test test = new Test();
        test.setScore(score);
        test.setTestFormat(testFormat);
        test.setUser(userService.findById(userId));
        testRepository.save(test);
        if (test.getScore() >= testFormat.getPassingScore()) {
            if(!courseCompleteService.checkExisted(userId,testFormat.getCourse().getId()))
            {
                courseCompleteService.create(userId, testFormat.getCourse().getId());
            }
        }
        return test;
    }
}

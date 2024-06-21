package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.QuestionDto;
import vn.aptech.pixelpioneercourse.dto.TestDto;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.entities.Test;
import vn.aptech.pixelpioneercourse.repository.TestRepository;

import java.util.Collections;
import java.util.List;

@Service
public class TestServiceImpl implements TestService{
    private final TestRepository testRepository;
    private final ModelMapper modelMapper;
    private final TestFormatService testFormatService;
    private final UserService userService;
    private final QuestionService questionService;

    public TestServiceImpl(TestRepository testRepository, ModelMapper modelMapper, TestFormatService testFormatService, UserService userService, QuestionService questionService) {
        this.testRepository = testRepository;
        this.modelMapper = modelMapper;
        this.testFormatService = testFormatService;
        this.userService = userService;
        this.questionService = questionService;
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


}

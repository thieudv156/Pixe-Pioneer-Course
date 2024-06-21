package vn.aptech.pixelpioneercourse.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.TestFormatCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.entities.TestFormat;
import vn.aptech.pixelpioneercourse.repository.TestFormatRepository;

@Service
public class TestFormatServiceImpl implements TestFormatService{
    private final TestFormatRepository testFormatRepository;
    private final ModelMapper modelMapper;
    private final CourseService courseService;
    private final QuestionService questionService;

    public TestFormatServiceImpl(TestFormatRepository testFormatRepository, ModelMapper modelMapper, CourseService courseService, QuestionService questionService) {
        this.testFormatRepository = testFormatRepository;
        this.modelMapper = modelMapper;
        this.courseService = courseService;
        this.questionService = questionService;
    }

    public TestFormat toTestFormat(TestFormatCreateDto dto){
        return modelMapper.map(dto, TestFormat.class);
    }

    public TestFormat findById(Integer id){
        return testFormatRepository.findById(id).orElse(null);
    }
    
    public TestFormat findByCourseId(Integer cID) {
    	return testFormatRepository.findByCourseId(cID);
    }

    public TestFormat update(TestFormatCreateDto dto, Integer id){
        TestFormat testFormat = findById(id);
        if(testFormat == null){
            throw new IllegalArgumentException("Test format not found: "+id);
        }
        if(checkTotalQuestion(dto.getTotalQuestion(),testFormat.getCourse().getId()))
        {
            testFormat.setDuration(dto.getDuration());
            testFormat.setPassingScore(dto.getPassingScore());
            testFormat.setTotalQuestion(dto.getTotalQuestion());
            return testFormatRepository.save(testFormat);
        }
        throw new IllegalArgumentException("Test format cannot have more total question then existed question");
    }

    public boolean checkTotalQuestion(Integer totalQuestion,Integer courseId) {
    	List<Question> questions = questionService.findByCourseId(courseId);
        return questions.size() >= totalQuestion;
    }

}

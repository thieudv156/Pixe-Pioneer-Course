package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.TestFormatCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.TestFormat;
import vn.aptech.pixelpioneercourse.repository.TestFormatRepository;

@Service
public class TestFormatServiceImpl implements TestFormatService{
    private final TestFormatRepository testFormatRepository;
    private final ModelMapper modelMapper;
    private final CourseService courseService;

    public TestFormatServiceImpl(TestFormatRepository testFormatRepository, ModelMapper modelMapper, CourseService courseService) {
        this.testFormatRepository = testFormatRepository;
        this.modelMapper = modelMapper;
        this.courseService = courseService;
    }

    public TestFormat toTestFormat(TestFormatCreateDto dto){
        return modelMapper.map(dto, TestFormat.class);
    }

    public TestFormat save(TestFormatCreateDto dto){
        Course course = courseService.findById(dto.getCourseId());
        if(course == null){
            throw new IllegalArgumentException("Course not found: "+dto.getCourseId());
        }
        TestFormat testFormat = toTestFormat(dto);
        testFormat.setCourse(course);
        return testFormatRepository.save(testFormat);
    }

    public TestFormat findById(Integer id){
        return testFormatRepository.findById(id).orElse(null);
    }

    public TestFormat update(TestFormatCreateDto dto, Integer id){
        TestFormat testFormat = findById(id);
        if(testFormat == null){
            throw new IllegalArgumentException("Test format not found: "+id);
        }
        testFormat.setDuration(dto.getDuration());
        testFormat.setPassingScore(dto.getPassingScore());
        testFormat.setTotalQuestion(dto.getTotalQuestion());
        return testFormatRepository.save(testFormat);
    }

    public boolean deleteById(Integer id){
        try{
            testFormatRepository.deleteById(id);
            return true;
        }
        catch (Exception e){
            throw new IllegalArgumentException("Test format not found: "+id);
        }
    }


}

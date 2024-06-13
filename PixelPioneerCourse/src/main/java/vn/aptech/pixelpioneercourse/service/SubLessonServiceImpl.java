package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.SubLessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Lesson;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.repository.SubLessonRepository;
import java.util.List;

@Service
public class SubLessonServiceImpl implements SubLessonService {

    final private SubLessonRepository subLessonRepository;
    final private ModelMapper modelMapper;
    final private LessonService lessonService;

    public SubLessonServiceImpl(SubLessonRepository subLessonRepository, ModelMapper modelMapper, LessonService lessonService) {
        this.subLessonRepository = subLessonRepository;
        this.modelMapper = modelMapper;
        this.lessonService = lessonService;
    }

    public SubLesson toSubLesson(SubLessonCreateDto dto) {
        return modelMapper.map(dto, SubLesson.class);
    }

    public List<SubLesson> findAllSubLessonsByLessonId(Integer lessonId) {
        try {
            return subLessonRepository.findByLessonIdOrderByOrderNumber(lessonId);
        } catch (Exception e) {
            throw new RuntimeException("List of SubLessons is null", e);
        }
    }
    
    public List<SubLesson> findAllSubLessons() {
    	try {
    		return subLessonRepository.findAll();
    	} catch (Exception e) {
    		throw new RuntimeException("Empty list");
    	}
    }

    public SubLesson findById(Integer id) {
        try {
            return subLessonRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public SubLesson createNewSublesson(Integer lessonId) {
        try {
            SubLesson subLesson = new SubLesson();
            Lesson lesson = lessonService.findById(lessonId);
            subLesson.setLesson(lesson);
            subLesson.setTitle("New Sub-Lesson");
            subLesson.setContent("Content");
            subLesson.setOrderNumber(lesson.getSubLessons().size() + 1);
            return subLessonRepository.save(subLesson);
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public boolean delete(Integer id) {
        try {
            subLessonRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public SubLesson update(Integer id, SubLessonCreateDto dto) {
        try {
            SubLesson subLesson = subLessonRepository.findById(id).orElse(null);
            if (subLesson == null) {
                throw new RuntimeException("SubLesson is null");
            }
            subLesson.setTitle(dto.getTitle());
            subLesson.setContent(dto.getContent());
            return subLessonRepository.save(subLesson);
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }
}
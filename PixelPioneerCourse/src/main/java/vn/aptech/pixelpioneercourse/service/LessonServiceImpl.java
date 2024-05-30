package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.LessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Lesson;
import vn.aptech.pixelpioneercourse.repository.LessonRepository;

import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {

    final private LessonRepository lessonRepository;
    final private ModelMapper modelMapper;

    public LessonServiceImpl(LessonRepository lessonRepository, ModelMapper modelMapper) {
        this.lessonRepository = lessonRepository;
        this.modelMapper = modelMapper;
    }

    public Lesson toLesson(LessonCreateDto dto) {return modelMapper.map(dto, Lesson.class);}

    public List<Lesson> findAllLessonByCourseId(Integer courseId) {
        try {
            return lessonRepository.findByCourseId(courseId);
        } catch (Exception e) {
            throw new RuntimeException("List of Lesson is null");
        }
    }

    public Lesson findById(Integer id) {
        try {
            return lessonRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Lesson is null");
        }
    }

    public Lesson save(LessonCreateDto dto) {
        try {
            Lesson lesson = toLesson(dto);
            return lessonRepository.save(lesson);
        } catch (Exception e) {
            throw new RuntimeException("Lesson is null");
        }
    }

    public boolean delete(Integer id) {
        try {
            lessonRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Lesson is null");
        }
    }

    public Lesson update(Integer id, LessonCreateDto dto) {
        try {
            Lesson lesson = lessonRepository.findById(id).orElse(null);
            if (lesson == null) {
                throw new RuntimeException("Lesson is null");
            }
            lesson.setTitle(dto.getTitle());
            lesson.setImage(dto.getImage());

            return lessonRepository.save(lesson);
        } catch (Exception e) {
            throw new RuntimeException("Lesson is null");
        }
    }

    public Lesson completeLesson(Integer id) {
        try {
            Lesson lesson = lessonRepository.findById(id).orElse(null);
            if (lesson == null) {
                throw new RuntimeException("Lesson is null");
            }
            lesson.setCompleteStatus(true);
            return lessonRepository.save(lesson);
        } catch (Exception e) {
            throw new RuntimeException("Lesson is null");
        }
    }

}

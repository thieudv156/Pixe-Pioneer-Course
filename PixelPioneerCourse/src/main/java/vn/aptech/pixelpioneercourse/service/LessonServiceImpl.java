package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.LessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Lesson;
import vn.aptech.pixelpioneercourse.repository.LessonRepository;

@Service
public class LessonServiceImpl implements LessonService {

    final private LessonRepository lessonRepository;
    final private ModelMapper modelMapper;

    public LessonServiceImpl(LessonRepository lessonRepository, ModelMapper modelMapper) {
        this.lessonRepository = lessonRepository;
        this.modelMapper = modelMapper;
    }

    public Lesson toLesson(LessonCreateDto dto) {return modelMapper.map(dto, Lesson.class);}


}

package vn.aptech.pixelpioneercourse.service;

import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.LessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Lesson;

import java.util.List;

public interface LessonService {
    List<Lesson> findAllLessonByCourseId(Integer courseId);
    Lesson findById(Integer id);
    Integer delete(Integer id);
    Lesson update(Integer id, LessonCreateDto dto);
    Lesson createNewLesson(Integer courseId);
}


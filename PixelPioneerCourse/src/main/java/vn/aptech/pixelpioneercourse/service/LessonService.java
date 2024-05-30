package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.LessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Lesson;

import java.util.List;

public interface LessonService {
    List<Lesson> findAllLessonByCourseId(Integer courseId);
    Lesson save(LessonCreateDto dto);
    boolean delete(Integer id);
    Lesson update(Integer id, LessonCreateDto dto);
    Lesson completeLesson(Integer id);
}

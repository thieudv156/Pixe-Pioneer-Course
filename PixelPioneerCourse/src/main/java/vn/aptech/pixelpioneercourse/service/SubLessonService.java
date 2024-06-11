package vn.aptech.pixelpioneercourse.service;

import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.SubLessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.SubLesson;

import java.util.List;

public interface SubLessonService {
    SubLesson toSubLesson(SubLessonCreateDto dto);
    List<SubLesson> findAllSubLessonsByLessonId(Integer lessonId);
    List<SubLesson> findAllSubLessons();
    SubLesson findById(Integer id);
    SubLesson createNewSublesson(Integer lessonId);
    boolean delete(Integer id);
    SubLesson update(Integer id, SubLessonCreateDto dto);
}

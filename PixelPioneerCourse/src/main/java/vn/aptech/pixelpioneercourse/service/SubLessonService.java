package vn.aptech.pixelpioneercourse.service;

import org.springframework.web.multipart.MultipartFile;
import vn.aptech.pixelpioneercourse.dto.SubLessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.SubLesson;

import java.util.List;

public interface SubLessonService {
    SubLesson toSubLesson(SubLessonCreateDto dto);
    List<SubLesson> findAllSubLessonsByLessonId(Integer lessonId);
    SubLesson findById(Integer id);
    SubLesson save(SubLessonCreateDto dto);
    boolean delete(Integer id);
    SubLesson update(Integer id, SubLessonCreateDto dto);
    SubLesson completeSubLesson(Integer id);
}

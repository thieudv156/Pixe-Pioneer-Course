package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Progress;
import vn.aptech.pixelpioneercourse.entities.SubLesson;

import java.util.List;

public interface ProgressService {
    List<Progress> createProgressByCourseId(Integer courseId, Integer userId);
    void finishSubLesson(Integer subLessonId, Integer userId);
    Double getCurrentProgressByCourseId(Integer courseId, Integer userId);
    SubLesson getCurrentSubLessonByCourseId(Integer courseId, Integer userId);
    void addProgressForNewSubLesson(Integer subLessonId);
}

package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.QuizCreateDto;

import vn.aptech.pixelpioneercourse.entities.Quiz;

import java.util.List;

public interface QuizService {

    List<Quiz> findAllQuizByLessonId(Integer lessonId);
    Quiz save(QuizCreateDto dto);
    boolean delete(Integer id);
    Quiz update(Integer id, QuizCreateDto dto);
    boolean checkAnswer(Integer id, String answer);
    Quiz findById(Integer id);
}

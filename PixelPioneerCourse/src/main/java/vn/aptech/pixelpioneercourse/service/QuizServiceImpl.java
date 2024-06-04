package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.QuizCreateDto;
import vn.aptech.pixelpioneercourse.entities.Quiz;
import vn.aptech.pixelpioneercourse.repository.QuizRepository;


import java.util.List;

@Service
public class QuizServiceImpl implements QuizService{

    private final QuizRepository quizRepository;
    private final ModelMapper modelMapper;
    private final LessonService lessonService;

    public QuizServiceImpl(QuizRepository quizRepository, ModelMapper modelMapper, LessonService lessonService) {
        this.quizRepository = quizRepository;
        this.modelMapper = modelMapper;
        this.lessonService = lessonService;
    }

    public Quiz toQuiz(QuizCreateDto dto) {
        return modelMapper.map(dto, Quiz.class);
    }

    public List<Quiz> findAllQuizByLessonId(Integer lessonId) {
        try {
            return quizRepository.findByLessonId(lessonId);
        } catch (Exception e) {
            throw new RuntimeException("List of Quiz is null");
        }
    }

    public Quiz findById(Integer id) {
        try {
            return quizRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Quiz is null");
        }
    }

    public Quiz save(QuizCreateDto dto) {
        try {
            Quiz quiz = toQuiz(dto);
            if(dto.getLessonId() > 0)
            {
                quiz.setLesson(lessonService.findById(dto.getLessonId()));
                return quizRepository.save(quiz);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Quiz is null");
        }
    }

    public boolean delete(Integer id) {
        try {
            quizRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Quiz is null");
        }
    }

    public Quiz update(Integer id, QuizCreateDto dto) {
        try {
            Quiz quiz = quizRepository.findById(id).orElse(null);
            if (quiz == null) {
                throw new RuntimeException("Quiz is null");
            }
            quiz.setQuestion(dto.getQuestion());
            quiz.setCorrectAnswer(dto.getCorrectAnswer());
            quiz.setWrongAnswer1(dto.getWrongAnswer1());
            quiz.setWrongAnswer2(dto.getWrongAnswer2());
            quiz.setWrongAnswer3(dto.getWrongAnswer3());
            return quizRepository.save(quiz);
        } catch (Exception e) {
            throw new RuntimeException("Quiz is null");
        }
    }

    public boolean checkAnswer(Integer id, String answer) {
        try {
            Quiz quiz = quizRepository.findById(id).orElse(null);
            if (quiz == null) {
                throw new RuntimeException("Quiz is null");
            }
            return quiz.getCorrectAnswer().equals(answer);
        } catch (Exception e) {
            throw new RuntimeException("Quiz is null");
        }
    }
}

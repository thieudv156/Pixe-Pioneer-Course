package vn.aptech.pixelpioneercourse.service;


import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.Quiz;
import vn.aptech.pixelpioneercourse.entities.Test;
import vn.aptech.pixelpioneercourse.repository.TestRepository;

import java.util.Collections;
import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final LessonService lessonService;
    private final QuizService quizService;

    public TestServiceImpl(TestRepository testRepository, LessonService lessonService, QuizService quizService) {
        this.testRepository = testRepository;
        this.lessonService = lessonService;
        this.quizService = quizService;
    }


    public Test createTest(Integer lessonId) {
        try {
            Test test = new Test();
            List<Quiz> allQuizzes = quizService.findAllQuizByLessonId(lessonId);
            Collections.shuffle(allQuizzes);
            List<Quiz> selectedQuizzes = allQuizzes.subList(0, Math.min(5, allQuizzes.size()));
            test.setQuizzes(selectedQuizzes);
            test.setScore(0);
            return testRepository.save(test);
        } catch (Exception e) {
            throw new RuntimeException("Error creating Test: ", e);
        }
    }

    public Test findById(int id) {
        try {
            return testRepository.findById(id).orElseThrow(() -> new RuntimeException("Test not found!"));
        } catch (Exception e) {
            throw new RuntimeException("Error finding test: "+e.getMessage());
        }
    }

    public Integer checkTest(Test test, List<String> userAnswers) {
        int score = 0;
        List<Quiz> quizzes = test.getQuizzes();

        for (int i = 0; i < quizzes.size(); i++) {
            Quiz quiz = quizzes.get(i);
            String userAnswer = userAnswers.get(i);

            if (quiz.getCorrectAnswer().equals(userAnswer)) {
                score += 20;
            }
        }

        return score;
    }

    public boolean completeTest(Integer testId, List<String> userAnswers) {
        try{
            Test test = findById(testId);
            if(test == null)
            {
                throw new RuntimeException("Test not found!");
            }
            test.setScore(checkTest(test, userAnswers));
            boolean isCompleted = test.getScore() >= 60;
            if (isCompleted) {
                lessonService.completeLesson(test.getLesson().getId());
            }
            testRepository.save(test);
            return isCompleted;
        }
        catch (Exception e){
            throw new RuntimeException("Error processing Test: ", e);
        }

    }

}




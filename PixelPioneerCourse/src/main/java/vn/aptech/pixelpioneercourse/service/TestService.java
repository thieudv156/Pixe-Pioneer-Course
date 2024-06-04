package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Test;

import java.util.List;

public interface TestService {
    Test createTest(Integer lessonId);
    Test findById(int id);
    Integer checkTest(Test test, List<String> userAnswers);
    boolean completeTest(Integer testId, List<String> userAnswers);
}

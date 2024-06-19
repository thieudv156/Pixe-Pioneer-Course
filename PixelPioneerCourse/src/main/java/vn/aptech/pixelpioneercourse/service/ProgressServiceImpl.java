package vn.aptech.pixelpioneercourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.*;
import vn.aptech.pixelpioneercourse.repository.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProgressServiceImpl implements ProgressService {
    private final ProgressRepository progressRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final SubLessonRepository subLessonRepository;

    @Autowired
    public ProgressServiceImpl(ProgressRepository progressRepository,
                               CourseRepository courseRepository,
                               UserRepository userRepository, SubLessonRepository subLessonRepository) {
        this.progressRepository = progressRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.subLessonRepository = subLessonRepository;
    }
    public List<Progress> createProgressByCourseId(Integer courseId, Integer userId) {
        List<Progress> progressList = new ArrayList<>();

        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<User> userOpt = userRepository.findById(userId);

        if (courseOpt.isPresent() && userOpt.isPresent()) {
            Course course = courseOpt.get();
            User user = userOpt.get();

            for (Lesson lesson : course.getLessons()) {
                for (SubLesson subLesson : lesson.getSubLessons()) {
                    Progress progress = new Progress();
                    progress.setSubLesson(subLesson);
                    progress.setUser(user);
                    progress.setIsCompleted(false);

                    progressRepository.save(progress);
                    progressList.add(progress);
                }
            }
        }

        return progressList;
    }

    public void finishSubLesson(Integer subLessonId, Integer userId) {
        Optional<Progress> progressOpt = progressRepository.findBySubLessonIdAndUserId(subLessonId, userId);

        if (progressOpt.isPresent()) {
            Progress progress = progressOpt.get();
            progress.setIsCompleted(true);
            progressRepository.save(progress);
        }
    }

    public Double getCurrentProgressByCourseId(Integer courseId, Integer userId) {
        Integer totalSubLessons = progressRepository.countTotalSubLessonsByCourseAndUser(courseId, userId);
        Integer completedSubLessons = progressRepository.countCompletedSubLessonsByCourseAndUser(courseId, userId);

        if (totalSubLessons == 0) {
            return 0.0;
        }
        BigDecimal progressPercentage = BigDecimal.valueOf((double) completedSubLessons / totalSubLessons * 100);
        BigDecimal roundedValue = progressPercentage.divide(BigDecimal.valueOf(0.5), 0, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0.5));


        return roundedValue.doubleValue();
    }

    public SubLesson getCurrentSubLessonByCourseId(Integer courseId, Integer userId) {
        List<SubLesson> currentSubLessonOpt = progressRepository.findFirstIncompleteSubLessonByUserIdAndCourseId(courseId, userId);
        Optional<SubLesson> currentSubLesson = Optional.ofNullable(currentSubLessonOpt.get(0));
        return currentSubLesson.orElse(null);
    }

    public void addProgressForNewSubLesson(Integer subLessonId) {
        Optional<SubLesson> subLessonOpt = subLessonRepository.findById(subLessonId);

        if (subLessonOpt.isPresent()) {
            SubLesson subLesson = subLessonOpt.get();
            Integer courseId = subLesson.getLesson().getCourse().getId();
            List<User> users = progressRepository.findUsersByCourseId(courseId);

            for (User user : users) {
                Progress progress = new Progress();
                progress.setSubLesson(subLesson);
                progress.setUser(user);
                progress.setIsCompleted(false);

                progressRepository.save(progress);
                return;
            }
        }
    }

}
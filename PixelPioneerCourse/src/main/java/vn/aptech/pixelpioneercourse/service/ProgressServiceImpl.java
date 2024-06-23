package vn.aptech.pixelpioneercourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
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
        List<Progress> existingProgress = progressRepository.findByCourseIdAndUserId(userId, courseId);
        if(!existingProgress.isEmpty()) {
            return existingProgress;
        }
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
        try {
            Optional<Progress> progressOpt = progressRepository.findBySubLessonIdAndUserId(subLessonId, userId);
            System.out.println(progressOpt);
            if (progressOpt.isPresent()) {
                Progress progress = progressOpt.get();
                progress.setIsCompleted(true);
                progressRepository.save(progress);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Double getCurrentProgressByCourseId(Integer courseId, Integer userId) {
        Integer totalSubLessons = progressRepository.countTotalSubLessonsByCourseAndUser(courseId, userId);
        Integer completedSubLessons = progressRepository.countCompletedSubLessonsByCourseAndUser(courseId, userId);
        if(!checkIfUserHasProgress(courseId, userId))
        {
            return null;
        }
        if (totalSubLessons == 0) {
            return 0.0;
        }
        BigDecimal progressPercentage = BigDecimal.valueOf((double) completedSubLessons / totalSubLessons * 100);
        BigDecimal roundedValue = progressPercentage.divide(BigDecimal.valueOf(0.5), 0, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0.5));


        return roundedValue.doubleValue();
    }

    public SubLesson getCurrentSubLessonByCourseId(Integer courseId, Integer userId) {
        List<SubLesson> currentSubLessonOpt = progressRepository.findFirstIncompleteSubLessonByUserIdAndCourseId(courseId, userId);
        if (currentSubLessonOpt.isEmpty()) {
            System.out.println(findLastSubLessonByCourseId(courseId, userId));
            return findLastSubLessonByCourseId(courseId, userId);
        }
        Optional<SubLesson> currentSubLesson = Optional.ofNullable(currentSubLessonOpt.get(0));
        System.out.println(currentSubLesson);
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
            }
        }
    }

    public SubLesson findLastSubLessonByCourseId(Integer courseId, Integer userId){
        List<SubLesson> subLessons = progressRepository.findFirstIncompleteSubLessonByUserIdAndCourseId(courseId, userId);
        if(subLessons.isEmpty())
        {
            List<Progress> progresses = progressRepository.findByCourseIdAndUserId(courseId, userId);
            return progresses.getLast().getSubLesson();
        }
        return subLessons.getFirst();
    }

    public List<Progress> findByUserId(Integer uid) {
    	return progressRepository.findByUserId(uid);
    }

    public Boolean checkIfUserHasProgress(Integer courseId, Integer userId) {
        List<Progress> progresses = progressRepository.findByCourseIdAndUserId(courseId, userId);
        return !progresses.isEmpty();
    }

    public List<Progress> checkAndCreateProgressForMissingSubLessons(Integer courseId, Integer userId) {
        List<Progress> existingProgress = progressRepository.findByCourseIdAndUserId(courseId, userId);
        if (existingProgress.isEmpty()) {
            return createProgressByCourseId(courseId, userId);
        }
        
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            for (Lesson lesson : course.getLessons()) {
                for (SubLesson subLesson : lesson.getSubLessons()) {
                    if (existingProgress.stream().noneMatch(p -> p.getSubLesson().getId().equals(subLesson.getId()))) {
                        Progress progress = new Progress();
                        progress.setSubLesson(subLesson);
                        progress.setUser(userRepository.findById(userId).get());
                        progress.setIsCompleted(false);
                        progressRepository.save(progress);
                        existingProgress.add(progress);
                    }
                }
            }
        }
        
        return existingProgress;
    }
    
    public List<Progress> findProgressByCourseIdAndUserId(Integer courseId, Integer userId) {
        return progressRepository.findByCourseIdAndUserId(courseId, userId);
    }
}

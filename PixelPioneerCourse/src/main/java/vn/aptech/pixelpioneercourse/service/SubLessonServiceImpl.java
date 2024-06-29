package vn.aptech.pixelpioneercourse.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.dto.SubLessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Lesson;
import vn.aptech.pixelpioneercourse.entities.Progress;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.repository.ProgressRepository;
import vn.aptech.pixelpioneercourse.repository.SubLessonRepository;
import java.util.List;
import java.util.Optional;

@Service
public class SubLessonServiceImpl implements SubLessonService {

    final private SubLessonRepository subLessonRepository;
    final private ModelMapper modelMapper;
    final private LessonService lessonService;
    private final ProgressService progressService;
    private final ProgressRepository progressRepository;

    public SubLessonServiceImpl(SubLessonRepository subLessonRepository, ModelMapper modelMapper, LessonService lessonService, ProgressService progressService, ProgressRepository progressRepository) {
        this.subLessonRepository = subLessonRepository;
        this.modelMapper = modelMapper;
        this.lessonService = lessonService;
        this.progressService = progressService;
        this.progressRepository = progressRepository;
    }

    public SubLesson toSubLesson(SubLessonCreateDto dto) {
        return modelMapper.map(dto, SubLesson.class);
    }

    public List<SubLesson> findAllSubLessonsByLessonId(Integer lessonId) {
        try {
            return subLessonRepository.findByLessonIdOrderByOrderNumber(lessonId);
        } catch (Exception e) {
            throw new RuntimeException("List of SubLessons is null", e);
        }
    }
    
    public List<SubLesson> findAllSubLessons() {
    	try {
    		return subLessonRepository.findAll();
    	} catch (Exception e) {
    		throw new RuntimeException("Empty list");
    	}
    }

    public SubLesson findById(Integer id) {
        try {
            return subLessonRepository.findById(id).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public SubLesson createNewSublesson(Integer lessonId) {
        try {
            SubLesson subLesson = new SubLesson();
            Lesson lesson = lessonService.findById(lessonId);
            subLesson.setLesson(lesson);
            subLesson.setTitle("New Sub-Lesson");
            subLesson.setContent("<h2>Content</h2>");
            subLesson.setOrderNumber(lesson.getSubLessons().size() + 1);
            SubLesson newSubLesson = subLessonRepository.save(subLesson);
            progressService.addProgressForNewSubLesson(subLesson.getId());
            return newSubLesson;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Integer delete(Integer id) {
        try {
            SubLesson deletedSubLesson = subLessonRepository.findById(id).orElse(null);
            if (deletedSubLesson == null) {
                throw new RuntimeException("SubLesson is null");
            }
            Integer lessonId = deletedSubLesson.getLesson().getId();
            checkDeleteCondition(id);
            subLessonRepository.deleteById(id);
            return lessonId;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SubLesson update(Integer id, SubLessonCreateDto dto) {
        try {
            SubLesson subLesson = subLessonRepository.findById(id).orElse(null);
            if (subLesson == null) {
                throw new RuntimeException("SubLesson is null");
            }
            subLesson.setTitle(dto.getTitle());
            subLesson.setContent(dto.getContent());
            return subLessonRepository.save(subLesson);
        } catch (Exception e) {
            throw new RuntimeException("SubLesson is null", e);
        }
    }

    public SubLesson finishSubLesson(Integer subLessonId, Integer userId) {
        // Fetch the current sub-lesson
        SubLesson subLesson = subLessonRepository.findById(subLessonId)
                .orElseThrow(() -> new RuntimeException("SubLesson not found"));

        // Get the course ID
        Integer courseId = subLesson.getLesson().getCourse().getId();

        // Fetch all progress entries for the user and course
        List<Progress> progresses = progressRepository.findByCourseIdAndUserId(courseId, userId);

        // Find the current progress for the sub-lesson
        Progress currentProgress = progresses.stream()
                .filter(progress -> progress.getSubLesson().getId().equals(subLessonId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Progress for the current sub-lesson not found"));

        // Check all previous progress entries by their IDs
        for (Progress progress : progresses) {
            if (progress.getId() < currentProgress.getId() && !progress.getIsCompleted()) {
                throw new RuntimeException("Previous sub-lesson is not finished");
            }
        }
        // Mark the current sub-lesson as finished
        progressService.finishSubLesson(subLessonId, userId);
        return subLesson;
    }

    public void checkDeleteCondition(Integer subLessonId) {
        SubLesson subLesson = subLessonRepository.findById(subLessonId).orElse(null);
        if (subLesson == null) {
            throw new RuntimeException("SubLesson is null");
        }
        Lesson lesson = subLesson.getLesson();
        Course course = lesson.getCourse();
        if (course.getIsPublished()) {
            if(lesson.getSubLessons().size() <= 2) {
                throw new RuntimeException("Lesson must have at least 2 sub-lessons to stay published!");
            }
        }
    }
}
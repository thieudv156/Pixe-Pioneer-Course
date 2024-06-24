package vn.aptech.pixelpioneercourse.service;

import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.CourseComplete;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.CourseCompleteRepository;

@Service
public class CourseCompleteService {
    private final CourseCompleteRepository courseCompleteRepository;
    private final CourseService courseService;
    private final UserService userService;

    public CourseCompleteService(CourseCompleteRepository courseCompleteRepository, CourseService courseService, UserService userService) {
        this.courseCompleteRepository = courseCompleteRepository;
        this.courseService = courseService;
        this.userService = userService;
    }

    public CourseComplete create(Integer userId, Integer courseId)
    {
        CourseComplete cc = new CourseComplete();
        cc.setUser(userService.findById(userId));
        cc.setCourse(courseService.findById(courseId));
        return courseCompleteRepository.save(cc);
    }
}

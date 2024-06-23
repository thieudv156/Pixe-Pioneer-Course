package vn.aptech.pixelpioneercourse.service;

import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.CourseComplete;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.CourseCompleteRepository;

@Service
public class CourseCompleteService {
    private final CourseCompleteRepository courseCompleteRepository;

    public CourseCompleteService(CourseCompleteRepository courseCompleteRepository) {
        this.courseCompleteRepository = courseCompleteRepository;
    }

    public CourseComplete create(User user, Course course)
    {
        CourseComplete cc = new CourseComplete();
        cc.setUser(user);
        cc.setCourse(course);
        return courseCompleteRepository.save(cc);
    }
}

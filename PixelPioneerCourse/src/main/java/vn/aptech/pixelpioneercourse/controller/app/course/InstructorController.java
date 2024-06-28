package vn.aptech.pixelpioneercourse.controller.app.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import vn.aptech.pixelpioneercourse.dto.CourseView;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.CourseService;
import vn.aptech.pixelpioneercourse.service.ReviewService;
import vn.aptech.pixelpioneercourse.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/app/instructor")
public class InstructorController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final UserService userService;

    @Autowired
    public InstructorController(CourseService courseService, ReviewService reviewService, UserService userService) {
        this.courseService = courseService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping
    public String index(@SessionAttribute("userId") Integer userId, Model model) {
        List<User> newEnrollUsers = userService.getNewEnrollmentsToday(userId);
        List<User> newCompletedUsers = userService.getCompletedCoursesToday(userId);
        List<User> allEnrollUser = userService.getAllEnrollmentsByInstructorId(userId);
        List<Course> courseList = courseService.findByInstructorId(userId);

        // Create a list of CourseView objects
        List<CourseView> courseViews = new ArrayList<>();
        for (Course course : courseList) {
            CourseView courseView = new CourseView();
            courseView.setCourse(course);
            courseView.setCompleteCount(courseService.getCompleteUserCount(course.getId()));
            courseView.setEnrollCount(courseService.getEnrollUserCount(course.getId()));
            courseView.setAvgReview(reviewService.average(reviewService.findByCourseId(course.getId())));
            courseViews.add(courseView);
        }

        model.addAttribute("courseViews", courseViews);
        model.addAttribute("newEnrollUsers", newEnrollUsers.size());
        model.addAttribute("newCompletedUsers", newCompletedUsers.size());
        model.addAttribute("allEnrollUser", allEnrollUser.size());
        return "app/instructor_view/instructor-dashboard";
    }
}

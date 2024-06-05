package vn.aptech.pixelpioneercourse.controller.client.course;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import vn.aptech.pixelpioneercourse.entities.Course;

import java.util.Arrays;
import java.util.List;

@Controller("clientCourseController")
@RequestMapping("/course")
public class ClientCourseController {
    @Value("${api.base.url}")
    private String apiBaseUrl;

    private String imageApiUrl;
    private String courseApiUrl;

    @PostConstruct
    public void init() {

        courseApiUrl = apiBaseUrl + "/course";
        imageApiUrl = apiBaseUrl + "/image";
    }

    @GetMapping("")
    public String index(Model model){
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(courseApiUrl, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        model.addAttribute("courses", courseList);
        return "course/index";
    }

    @GetMapping("/instructor/{instructorId}")
    public String showCourseByInstructorId(Model model, @PathVariable("instructorId") Integer instructorId){
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(courseApiUrl + "/instructor/" + instructorId, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        model.addAttribute("courses", courseList);
        model.addAttribute("imageApiUrl", imageApiUrl);
        return "course/instructor/course-dashboard";
    }

    @GetMapping("/{id}")
    public String showCourseDetail(Model model, @PathVariable("id") Integer id){
        RestTemplate restTemplate = new RestTemplate();
        Course course = restTemplate.getForObject(courseApiUrl + "/" + id, Course.class);
        model.addAttribute("course", course);
        model.addAttribute("imageApiUrl", imageApiUrl);
        return "course/instructor/course-detail";
    }
}
package vn.aptech.pixelpioneercourse.controller.client;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import vn.aptech.pixelpioneercourse.entities.Course;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Controller("clientCourseController")
@RequestMapping("/course")
public class ClientCourseController {
    @Value("${api.base.url}")
    private String apiBaseUrl;

    private String apiUrl;

    @PostConstruct
    public void init() {
        apiUrl = apiBaseUrl + "/course";
    }

   @GetMapping("")
    public String index(Model model){
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(apiUrl, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        model.addAttribute("courses", courseList);
        return "course/index";
    }
}
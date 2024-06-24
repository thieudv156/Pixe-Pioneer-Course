package vn.aptech.pixelpioneercourse.controller.app.home;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.service.CategoryService;
import vn.aptech.pixelpioneercourse.service.CourseService;


import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String index(HttpSession session, Model model) {
        List<Category> categories = categoryService.findAll();
        Map<Category, List<Course>> categoryCourses = new HashMap<>();

        for (Category category : categories) {
            List<Course> courses = courseService.findTop8ByCategoryOrderByCreatedAtDesc(category);
            System.out.println(courses);
            if (!courses.isEmpty()) {
                categoryCourses.put(category, courses);
            }
        }

        model.addAttribute("categoryCourses", categoryCourses);
        return "app/index";
    }
}
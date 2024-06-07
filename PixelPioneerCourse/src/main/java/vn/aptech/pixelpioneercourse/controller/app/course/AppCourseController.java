package vn.aptech.pixelpioneercourse.controller.app.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.pixelpioneercourse.dto.CourseCreateDto;
import vn.aptech.pixelpioneercourse.entities.Category;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Image;
import vn.aptech.pixelpioneercourse.entities.Lesson;

import java.util.*;

@Controller("clientCourseController")
@RequestMapping("/app/course")
public class AppCourseController {
    @Value("${api.base.url}")
    private String apiBaseUrl;

    private String imageApiUrl;
    private String courseApiUrl;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;

    public AppCourseController(ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

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
        model.addAttribute("pageTitle", "My Courses");
        return "course/instructor/course-dashboard";
    }


    @GetMapping("/{id}")
        public String showCourseById(Model model, @PathVariable("id") Integer id){
        RestTemplate restTemplate = new RestTemplate();
        Optional<Course> course = Optional.ofNullable(restTemplate.getForObject(courseApiUrl + "/" + id, Course.class));
        if(course.isEmpty()){
            return "redirect:/app/course/instructor/1";
        }
        Optional<Category[]> categories = Optional.ofNullable(restTemplate.getForObject(apiBaseUrl + "/category", Category[].class));
        List<Category> categoryList = Arrays.asList(categories.get());
        CourseCreateDto courseCreateDto = modelMapper.map(course.get(), CourseCreateDto.class);
        if (course.get().getFrontPageImage() == null) {
            model.addAttribute("oldImageUrl", imageApiUrl + "/default.jpg");
        } else {
            model.addAttribute("oldImageUrl", imageApiUrl + "/" + course.get().getFrontPageImage().getImageName());
        }

        List<Lesson> lessons = course.get().getLessons();
        model.addAttribute("courseCreateDto", courseCreateDto);
        model.addAttribute("courseId", course.get().getId());
        model.addAttribute("categories", categoryList);
        model.addAttribute("pageTitle", "Course detail");
        model.addAttribute("lessons", lessons);
        return "course/instructor/course-detail";
    }

    @PostMapping("/{id}/update")
    public String updateCourse(@ModelAttribute CourseCreateDto courseCreateDto,
                               @RequestParam(value = "image",required = false) MultipartFile image,
                               @PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        try {
            // Convert CourseCreateDto to JSON string
            String courseData = objectMapper.writeValueAsString(courseCreateDto);
            System.out.println(courseData);

            // Create a MultiValueMap to hold the parts
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("courseData", courseData);

            // Handle file upload
            if (!image.isEmpty()) {
                body.add("image", image.getResource());
            }

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Create HttpEntity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Make the API call to update the course
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Course> response = restTemplate.exchange(courseApiUrl + "/" + id+ "/update", HttpMethod.PUT, requestEntity, Course.class);
            Course updatedCourse = response.getBody();

            if(updatedCourse == null){
                redirectAttributes.addFlashAttribute("errorMessage", "Course not found!");
                return "redirect:/app/course/" + id + "/update";  // Redirect back to the course update form
            }
            redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
            return "redirect:/app/course/instructor/1";  // Redirect to the course detail page
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating course: " + e.getMessage());

            return "redirect:/app/course/" + id;  // Redirect back to the course update form
        }
    }

    @GetMapping("/create-course")
    public String createCourse(RedirectAttributes redirectAttributes){

        try {
            RestTemplate restTemplate = new RestTemplate();
            Course course = restTemplate.getForObject(courseApiUrl + "/create-course", Course.class);
            if (course == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error creating course");
                return "redirect:/app/course/instructor/1";
            }
            redirectAttributes.addFlashAttribute("successMessage", "Course created successfully");
            return "redirect:/app/course/" + course.getId();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCourse(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(courseApiUrl + "/" + id + "/delete");
            redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully");
            return "redirect:/app/course/instructor/1";
        } catch (RestClientException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting course: " + e.getMessage());
            return "redirect:/app/course/instructor/1";
        }
    }

}
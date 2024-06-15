package vn.aptech.pixelpioneercourse.controller.app.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.relational.core.sql.In;
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
import vn.aptech.pixelpioneercourse.entities.Lesson;
import vn.aptech.pixelpioneercourse.entities.SubLesson;

import java.util.*;
import java.util.stream.Collectors;

@Controller("clientCourseController")
@RequestMapping("/app/course")
public class AppCourseController{
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


    @GetMapping("/")
    public String index(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 12; // Number of courses per page
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(courseApiUrl, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        Category[] categories = restTemplate.getForObject(courseApiUrl + "/categories", Category[].class);
        List<Category> categoryList = Arrays.asList(categories);

        int totalCourses = courseList.size();
        int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

        // Ensure the page number is within the valid range
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalCourses);

        // Ensure start index is not negative
        if (start < 0) {
            start = 0;
        }

        List<Course> courses = courseList.subList(start, end);
        model.addAttribute("categories", categoryList);
        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("imageApiUrl", imageApiUrl);
        model.addAttribute("totalCourses", totalCourses);
        return "app/user_view/course/index";
    }



    @GetMapping("/category")
    public String sortByCategory(Model model,
                                 @RequestParam(value = "page", defaultValue = "1") int page,
                                 @RequestParam(value = "category", required = false) String category) {
        int pageSize = 12; // Number of courses per page
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(courseApiUrl, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        Category[] categories = restTemplate.getForObject(courseApiUrl + "/categories", Category[].class);
        List<Category> categoryList = Arrays.asList(categories);

        // Filter courses based on the category parameter
        if (category != null && !category.isEmpty()) {
            courseArray = restTemplate.getForObject(courseApiUrl + "/category/" + category, Course[].class);
            courseList = Arrays.asList(courseArray);
        }

        int totalCourses = courseList.size();
        int totalPages = (int) Math.ceil((double) totalCourses / pageSize);

        // Ensure the page number is within the valid range
        if (page < 1) {
            page = 1;
        } else if (page > totalPages) {
            page = totalPages;
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalCourses);

        List<Course> courses = courseList.subList(start, end);
        model.addAttribute("categories", categoryList);
        model.addAttribute("courses", courses);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("imageApiUrl", imageApiUrl);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("selectedCategory", category);
        return "app/user_view/course/index";
    }

    @GetMapping("/view/{id}")
    public String showCourseByIdAndLessonId(Model model,
                                            @PathVariable("id") Integer id,
                                            @RequestParam(value = "lessonOrder",defaultValue = "1") Integer lessonOrder,
                                            @RequestParam(value = "subLessonOrder",defaultValue = "1") Integer subLessonOrder){
        RestTemplate restTemplate = new RestTemplate();
        Optional<Course> course = Optional.ofNullable(restTemplate.getForObject(courseApiUrl + "/" + id, Course.class));
        if(course.isEmpty()){
            return "redirect:/app/course";
        }
        List<Lesson> lessons = course.get().getLessons();
        HashMap<Integer,SubLesson> subLessonHashMap = new HashMap<>();
        for (Lesson lesson : lessons) {
            for (SubLesson subLesson : lesson.getSubLessons()) {
                subLessonHashMap.put(subLesson.getId(),subLesson);
            }
        }
        Lesson currentLesson = lessons.stream().filter(lesson -> lesson.getOrderNumber().equals(lessonOrder)).toList().getFirst();
        SubLesson currentSubLesson = currentLesson.getSubLessons().stream().filter(subLesson -> subLesson.getOrderNumber().equals(subLessonOrder)).toList().getFirst();
        model.addAttribute("currentLesson",currentLesson);
        model.addAttribute("currentSubLesson",currentSubLesson);
        model.addAttribute("subLessonHashMap",subLessonHashMap);
        model.addAttribute("lessons", lessons);
        model.addAttribute("course", course.get());
        model.addAttribute("pageTitle", "Course detail");
        return "app/user_view/course/course-view";
    }

    @GetMapping("/instructor/courses/{instructorId}")
    public String showCourseByInstructorId(Model model, @PathVariable("instructorId") Integer instructorId){
        RestTemplate restTemplate = new RestTemplate();
        Course[] courseArray = restTemplate.getForObject(courseApiUrl + "/instructor/" + instructorId, Course[].class);
        List<Course> courseList = Arrays.asList(courseArray);
        model.addAttribute("courses", courseList);
        model.addAttribute("imageApiUrl", imageApiUrl);
        model.addAttribute("pageTitle", "My Courses");
        return "app/instructor_view/course/course-dashboard";
    }


    @GetMapping("/instructor/view/{id}")
        public String showCourseById(Model model, @PathVariable("id") Integer id, @SessionAttribute("userId") Integer userId){
        RestTemplate restTemplate = new RestTemplate();
        Optional<Course> course = Optional.ofNullable(restTemplate.getForObject(courseApiUrl + "/" + id, Course.class));
        if(course.isEmpty()){
            return "redirect:/app/course/instructor/courses/"+userId;
        }
        Optional<Category[]> categories = Optional.ofNullable(restTemplate.getForObject(apiBaseUrl + "/category", Category[].class));
        List<Category> categoryList = Arrays.asList(categories.get());
        CourseCreateDto courseCreateDto = modelMapper.map(course.get(), CourseCreateDto.class);
        if (course.get().getFrontPageImage() == null) {
            model.addAttribute("oldImageUrl", imageApiUrl + "/default.jpg");
        } else {
            model.addAttribute("oldImageUrl", imageApiUrl + "/" + course.get().getFrontPageImage().getImageName());
        }
        if((course.get().getCategory()) != null){
            model.addAttribute("categoryId", course.get().getCategory().getId());
        }
        List<Lesson> lessons = course.get().getLessons();
        HashMap<Integer,SubLesson> subLessonHashMap = new HashMap<>();
        for (Lesson lesson : lessons) {
            for (SubLesson subLesson : lesson.getSubLessons()) {
                subLessonHashMap.put(subLesson.getId(),subLesson);
            }
        }
        model.addAttribute("isPublished", course.get().getIsPublished());
        model.addAttribute("subLessonHashMap",subLessonHashMap);
        model.addAttribute("courseCreateDto", courseCreateDto);
        model.addAttribute("courseId", course.get().getId());
        model.addAttribute("categories", categoryList);
        model.addAttribute("pageTitle", "Course detail");
        model.addAttribute("lessons", lessons);
        return "app/instructor_view/course/course-detail";
    }

    @PostMapping("/instructor/{id}/update")
    public String updateCourse(@ModelAttribute CourseCreateDto courseCreateDto,
                               @RequestParam(value = "image",required = false) MultipartFile image,
                               @PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes,
                               @SessionAttribute("userId") Integer userId){
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
                return "redirect:/app/course/instructor/view/" + id;  // Redirect back to the course update form
            }
            redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
            return "redirect:/app/course/instructor/courses/"+userId;  // Redirect to the course detail page
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating course: " + e.getMessage());

            return "redirect:/app/course/instructor/view/" + id;  // Redirect back to the course update form
        }
    }

    @GetMapping("/instructor/create-course")
    public String createCourse(RedirectAttributes redirectAttributes, @SessionAttribute("userId") Integer userId){

        try {
            RestTemplate restTemplate = new RestTemplate();
            Course course = restTemplate.getForObject(courseApiUrl + "/create-course/"+userId, Course.class);
            if (course == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error creating course");
                return "redirect:/app/course/instructor/"+userId;
            }
            redirectAttributes.addFlashAttribute("successMessage", "Course created successfully");
            return "redirect:/app/course/instructor/view/" + course.getId();
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/instructor/{id}/delete")
    public String deleteCourse(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes,@SessionAttribute("userId") Integer userId){
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.delete(courseApiUrl + "/" + id + "/delete");
            redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully");
            return "redirect:/app/course/instructor/courses/"+userId;
        } catch (RestClientException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting course: " + e.getMessage());
            return "redirect:/app/course/instructor/view/"+id;
        }
    }

    @GetMapping("/instructor/{courseId}/publish")
    public String publishCourse(@PathVariable("courseId") Integer courseId, RedirectAttributes redirectAttributes, @SessionAttribute("userId") Integer userId){
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(courseApiUrl + "/" + courseId + "/publish", null);
            redirectAttributes.addFlashAttribute("successMessage", "Course published successfully");
            return "redirect:/app/course/instructor/courses/" + userId;
        } catch (RestClientException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error publishing course: " + e.getMessage());
            return "redirect:/app/course/instructor/view/" + courseId;
        }
    }

    @GetMapping("/instructor/{courseId}/unpublish")
    public String unpublishCourse(@PathVariable("courseId") Integer courseId, RedirectAttributes redirectAttributes){
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(courseApiUrl + "/" + courseId + "/unpublish", null);
            redirectAttributes.addFlashAttribute("successMessage", "Course unpublished successfully");
            return "redirect:/app/course/instructor/view/" + courseId;
        } catch (RestClientException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error unpublishing course: " + e.getMessage());
            return "redirect:/app/course/instructor/view/" + courseId;
        }
    }

    @GetMapping("/view")
    public String viewCourse(){
        return "app/course/course-view";
    }

}
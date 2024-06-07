package vn.aptech.pixelpioneercourse.controller.app.lesson;

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
import vn.aptech.pixelpioneercourse.dto.LessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Course;
import vn.aptech.pixelpioneercourse.entities.Lesson;

import java.util.Optional;

@Controller
@RequestMapping("/app/lesson")
public class AppLessonContronller {
    @Value("${api.base.url}")
    private String apiBaseUrl;

    private String imageApiUrl;
    private String lessonApiUrl;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;


    public AppLessonContronller(ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        lessonApiUrl = apiBaseUrl + "/lesson";
        imageApiUrl = apiBaseUrl + "/image";
    }
    @GetMapping("/course/{courseId}/create-lesson")
    public String createNewLesson(@PathVariable("courseId") Integer courseId, RedirectAttributes redirectAttributes) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Lesson lesson = restTemplate.getForObject(lessonApiUrl +"/course/"+ courseId+ "/create-lesson", Lesson.class);
            if (lesson == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Error creating course");
                return "redirect:/app/course/instructor/1";
            }
            redirectAttributes.addFlashAttribute("successMessage", "Course created successfully");
            return "redirect:/app/course/" + courseId;
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{lessonId}")
    public String showLessonById(Model model, @PathVariable("lessonId") Integer lessonId){
        RestTemplate restTemplate = new RestTemplate();
        Optional<Lesson> lesson = Optional.ofNullable(restTemplate.getForObject(lessonApiUrl + "/" + lessonId, Lesson.class));
        LessonCreateDto lessonCreateDto = modelMapper.map(lesson, LessonCreateDto.class);
        model.addAttribute("lessonCreateDto", lessonCreateDto);
        if (lesson.get().getFrontPageImage() == null) {
            model.addAttribute("oldImageUrl", imageApiUrl + "/default.jpg");
        } else {
            model.addAttribute("oldImageUrl", imageApiUrl + "/" + lesson.get().getFrontPageImage().getImageName());
        }
        return "lesson/instructor/lesson-detail";
    }

    @PostMapping("/{lessonId}/update")
    public String updateLesson(@ModelAttribute LessonCreateDto lessonCreateDto,
                               @RequestParam(value = "image",required = false) MultipartFile image,
                               @PathVariable("lessonId") Integer lessonId,
                               RedirectAttributes redirectAttributes){
        try {
            // Convert CourseCreateDto to JSON string
            String lessonData = objectMapper.writeValueAsString(lessonCreateDto);
            System.out.println(lessonData);

            // Create a MultiValueMap to hold the parts
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("lessonData", lessonData);
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
            ResponseEntity<Lesson> response = restTemplate.exchange(lessonApiUrl + "/" + lessonId+ "/update", HttpMethod.PUT, requestEntity, Lesson.class);
            Lesson updatedLesson = response.getBody();

            if(updatedLesson == null){
                redirectAttributes.addFlashAttribute("errorMessage", "Lesson not found!");
                return "redirect:/app/lesson/" + lessonId;  // Redirect back to the course update form
            }
            redirectAttributes.addFlashAttribute("successMessage", "Lesson updated successfully!");
            return "redirect:/app/lesson/"+lessonId;  // Redirect to the course detail page
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating lesson: " + e.getMessage());
            return "redirect:/app/lesson/" + lessonId;  // Redirect back to the course update form
        }
    }
}

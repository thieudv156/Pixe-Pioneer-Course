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
import vn.aptech.pixelpioneercourse.dto.LessonCreateDto;
import vn.aptech.pixelpioneercourse.entities.Lesson;

import java.util.Optional;

@Controller
@RequestMapping("/app/lesson")
public class AppLessonController {
    @Value("${api.base.url}")
    private String apiBaseUrl;
    private String lessonApiUrl;
    private final ModelMapper modelMapper;


    public AppLessonController( ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        lessonApiUrl = apiBaseUrl + "/lesson";
    }
    @GetMapping("/instructor/course/{courseId}/create-lesson")
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

    @GetMapping("/instructor/view/{lessonId}")
    public String showLessonById(Model model, @PathVariable("lessonId") Integer lessonId){
        RestTemplate restTemplate = new RestTemplate();
        Optional<Lesson> lesson = Optional.ofNullable(restTemplate.getForObject(lessonApiUrl + "/" + lessonId, Lesson.class));
        if (lesson.isEmpty()) {
            model.addAttribute("errorMessage", "Lesson not found!");
            return "app/lesson/instructor/lesson-detail";
        }
        LessonCreateDto lessonCreateDto = modelMapper.map(lesson, LessonCreateDto.class);
        model.addAttribute("lessonCreateDto", lessonCreateDto);
        model.addAttribute("subLessons", lesson.get().getSubLessons());
        model.addAttribute("courseId", lesson.get().getCourse().getId());
        return "app/instructor_view/lesson/lesson-detail";
    }

    @PostMapping("/instructor/{lessonId}/update")
    public String updateLesson(@ModelAttribute LessonCreateDto lessonCreateDto,
                               @PathVariable Long lessonId,
                               RedirectAttributes redirectAttributes) {
        try {
            // Create HttpEntity with the LessonCreateDto
            HttpEntity<LessonCreateDto> requestEntity = new HttpEntity<>(lessonCreateDto);

            // Make the API call to update the lesson
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Lesson> response = restTemplate.exchange(
                    lessonApiUrl + "/" + lessonId + "/update",
                    HttpMethod.PUT,
                    requestEntity,
                    Lesson.class
            );

            Lesson updatedLesson = response.getBody();

            if (updatedLesson == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Lesson not found!");
                return "redirect:/app/lesson/instructor/view/" + lessonId;
            }

            redirectAttributes.addFlashAttribute("successMessage", "Lesson updated successfully!");
            return "redirect:/app/lesson/instructor/view/" + lessonId;
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating lesson: " + e.getMessage());
            return "redirect:/app/lesson/instructor/view/" + lessonId;
        }
    }

    @GetMapping("/instructor/{lessonId}/delete")
    public String deleteLesson(@PathVariable("lessonId") Integer lessonId, RedirectAttributes redirectAttributes, @SessionAttribute("userId") Integer userId){
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Integer> response = restTemplate.exchange(
                    lessonApiUrl + "/" + lessonId + "/delete",
                    HttpMethod.DELETE,
                    null,
                    Integer.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                Integer courseId = response.getBody();
                redirectAttributes.addFlashAttribute("successMessage", "Lesson deleted successfully");
                return "redirect:/app/course/instructor/view/" + courseId;
            } else {
                throw new RestClientException("Error deleting lesson");
            }
        } catch (RestClientException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting lesson: " + e.getMessage());
            return "redirect:/app/course/instructor/courses/"+userId;
        }
    }

}

package vn.aptech.pixelpioneercourse.controller.app.course;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.pixelpioneercourse.dto.QuestionCreateDto;
import vn.aptech.pixelpioneercourse.dto.TestFormatCreateDto;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.service.QuestionService;
import vn.aptech.pixelpioneercourse.service.TestFormatService;

@Controller
@RequestMapping("/app/question")
public class AppQuestionController {

    private final QuestionService questionService;
    private final TestFormatService testFormatService;
    private final ModelMapper modelMapper;

    public AppQuestionController(QuestionService questionService, TestFormatService testFormatService, ModelMapper modelMapper) {
        this.questionService = questionService;
        this.testFormatService = testFormatService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/test-hub/{courseId}")
    public String showQuestionByCourseId(@PathVariable("courseId") Integer courseId, Model model){
        TestFormatCreateDto testFormatDto = modelMapper.map(testFormatService.findByCourseId(courseId), TestFormatCreateDto.class);
        int totalDuration = testFormatDto.getDuration();
        testFormatDto.setDurationMinutes(totalDuration / 60);
        testFormatDto.setDurationSeconds(totalDuration % 60);
        model.addAttribute("testFormatDto", testFormatDto);
        model.addAttribute("questions",questionService.findByCourseId(courseId));
        model.addAttribute("courseId",courseId);
        return "/app/instructor_view/course/test-hub";
    }

    @PostMapping("/test-hub/{courseId}/update")
    public String updateTestFormat(@PathVariable("courseId") Integer courseId, @Valid @ModelAttribute("testFormatDto") TestFormatCreateDto testFormatDto, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("questions", questionService.findByCourseId(courseId));
            model.addAttribute("courseId", courseId);
            return "/app/instructor_view/course/test-hub";
        }

        try {
            testFormatDto.setDuration(testFormatDto.getDurationMinutes() * 60 + testFormatDto.getDurationSeconds());
            testFormatService.update(testFormatDto, courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Update Test Format successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/app/question/test-hub/" + courseId;
    }

    @GetMapping("/{courseId}/create")
    public String createQuestionByCourseId(@PathVariable("courseId") Integer courseId, Model model){
        model.addAttribute("questionCreateDto",new QuestionCreateDto());
        model.addAttribute("courseId",courseId);
        return "/app/instructor_view/course/question-create";
    }

    @PostMapping("/{courseId}/create")
    public String createQuestion(@PathVariable("courseId") Integer courseId, @Valid QuestionCreateDto questionCreateDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("courseId", courseId);
            return "/app/instructor_view/course/question-create"; // replace with your Thymeleaf template path
        }
        questionService.save(questionCreateDto);
        return "redirect:/app/question/test-hub/" + courseId;
    }

    @GetMapping("/{questionId}/update")
    public String updateQuestion(@PathVariable("questionId") Integer questionId, Model model){
        Question question = questionService.findById(questionId);
        QuestionCreateDto dto = modelMapper.map(question,QuestionCreateDto.class);
        model.addAttribute("questionCreateDto",dto);
        model.addAttribute("courseId",question.getCourse().getId());
        return "app/instructor_view/course/question-update";
    }

    @PostMapping("/{questionId}/update")
    public String updateQuestion(@PathVariable("questionId") Integer questionId,@Valid @ModelAttribute QuestionCreateDto questionCreateDto){
        System.out.println(questionCreateDto);
        Question updatedQuestion = questionService.update(questionCreateDto, questionId);
        return "redirect:/app/question/test-hub/"+updatedQuestion.getCourse().getId();
    }
}

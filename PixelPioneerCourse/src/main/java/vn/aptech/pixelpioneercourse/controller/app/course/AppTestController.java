package vn.aptech.pixelpioneercourse.controller.app.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.pixelpioneercourse.dto.TestDto;
import vn.aptech.pixelpioneercourse.entities.Test;
import vn.aptech.pixelpioneercourse.service.TestService;

@Controller
@RequestMapping("/app/test")
public class AppTestController {

    private final TestService testService;

    @Autowired
    public AppTestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/start/{testFormatId}/{courseId}")
    public String startTest(@SessionAttribute("userId") Integer userId, @PathVariable Integer testFormatId,@PathVariable("courseId") Integer courseId, Model model, RedirectAttributes redirectAttributes) {
        try{
            TestDto testDto = testService.createTestDto(testFormatId, userId);
            model.addAttribute("testDto", testDto);
            return "app/user_view/test/start";
        }
        catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/app/course/view/"+courseId;
        }
    }

    @PostMapping("/submit")
    public String submitTest(@SessionAttribute("userId") Integer userId, @ModelAttribute("testDto") TestDto testDto, Model model) {
        System.out.println(testDto); // Debugging line to print the submitted TestDto
        Test submittedTest = testService.submitTest(testDto, userId);
        model.addAttribute("submittedTest", submittedTest);
        return "app/user_view/test/result";
    }
}

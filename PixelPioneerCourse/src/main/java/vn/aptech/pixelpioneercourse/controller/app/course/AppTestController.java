package vn.aptech.pixelpioneercourse.controller.app.course;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.aptech.pixelpioneercourse.dto.TestDto;
import vn.aptech.pixelpioneercourse.entities.Question;
import vn.aptech.pixelpioneercourse.service.TestService;

import java.util.List;

@Controller
@RequestMapping("/app/test")
public class AppTestController {

    private final TestService testService;

    public AppTestController(TestService testService) {
        this.testService = testService;
    }


    @GetMapping("/{testFormatId}")
    public String createTest(@PathVariable("testFormatId") Integer testFormatId, @SessionAttribute("userId") Integer userId, Model model, RedirectAttributes redirectAttributes){
        TestDto createTestDto = testService.createTestDto(testFormatId, userId);
        System.out.println(createTestDto);
        model.addAttribute("test", createTestDto);
        redirectAttributes.addFlashAttribute("successMessage", "Create Test successfully, you now have"+createTestDto.getDuration()+" minutes to finish the test");
        return "app/user_view/course/test";
    }


}

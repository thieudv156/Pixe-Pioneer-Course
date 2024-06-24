package vn.aptech.pixelpioneercourse.controller.app.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.TestDto;
import vn.aptech.pixelpioneercourse.entities.Test;
import vn.aptech.pixelpioneercourse.service.TestService;

@Controller
@RequestMapping("/app/tests")
public class AppTestController {

    private final TestService testService;

    @Autowired
    public AppTestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/start/{testFormatId}")
    public String startTest(@SessionAttribute("userId") Integer userId, @PathVariable Integer testFormatId, Model model) {
        TestDto testDto = testService.createTestDto(testFormatId, userId);
        model.addAttribute("testDto", testDto);
        return "app/user_view/test/start"; // Thymeleaf template for starting the test
    }

    @PostMapping("/submit")
    public String submitTest(@SessionAttribute("userId") Integer userId, @ModelAttribute("testDto") TestDto testDto, Model model) {
        // Assuming userId is retrieved from authentication or session
        Test submittedTest = testService.submitTest(testDto, userId);
        model.addAttribute("submittedTest", submittedTest);
        return "app/user_view/test/result";
    }
}

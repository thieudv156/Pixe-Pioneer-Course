package vn.aptech.pixelpioneercourse.controller.app.course;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.aptech.pixelpioneercourse.service.QuestionService;

@Controller
@RequestMapping("/app/question")
public class AppQuestionController {

    private final QuestionService questionService;

    public AppQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/{courseId}")
    public String showQuestionByCourseId(@PathVariable("courseId") Integer courseId){


        return "app/question/index";
    }
}

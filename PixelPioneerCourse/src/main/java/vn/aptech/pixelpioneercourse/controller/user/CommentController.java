package vn.aptech.pixelpioneercourse.controller.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.ReviewCreateDto;
import vn.aptech.pixelpioneercourse.entities.Review;
import vn.aptech.pixelpioneercourse.service.CommentService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
//@RequestMapping("/app/course") //co the them id cua tung course
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping("/postcomment")
    public String postComment(@ModelAttribute("reviewCreateDto") @Valid ReviewCreateDto reviewCreateDto,
                              BindingResult result,
                              Model model) {
        if (result.hasErrors()) {
            model.addAttribute("reviewCreateDto", reviewCreateDto);
            return "course/comment_form";
        }
        commentService.createComment(reviewCreateDto);
        return "redirect:/app/course/comments";
    }

    @GetMapping("/comments")
    public String getAllComments(Model model) {
        List<Review> comments = commentService.getAllComments();
        model.addAttribute("comments", comments);
        return "course/comments_list";
    }
    
    @GetMapping("/comment/{id}")
    public String getCommentById(@PathVariable int id, Model model) {
        Optional<Review> comment = commentService.getCommentById(id);
        if (comment.isPresent()) {
            model.addAttribute("comment", comment.get());
        } else {
            model.addAttribute("error", "Comment not found");
        }
        return "course/comment_detail";
    }

    @PostMapping("/comment/update/{id}")
    public String updateComment(@PathVariable int id,
                                @ModelAttribute("reviewCreateDto") @Valid ReviewCreateDto reviewCreateDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            model.addAttribute("reviewCreateDto", reviewCreateDto);
            return "course/comment_form";
        }
        commentService.updateComment(id, reviewCreateDto);
        return "redirect:/app/course/comments";
    }

    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable int id) {
        commentService.deleteComment(id);
        return "redirect:/app/course/comments";
    }
}

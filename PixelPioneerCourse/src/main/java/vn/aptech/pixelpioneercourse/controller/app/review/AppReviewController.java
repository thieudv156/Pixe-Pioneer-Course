package vn.aptech.pixelpioneercourse.controller.app.review;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.aptech.pixelpioneercourse.dto.ReviewCreateDto;
import vn.aptech.pixelpioneercourse.entities.Review;
import vn.aptech.pixelpioneercourse.service.ReviewService;

import java.util.List;

@Controller
@RequestMapping("/app/reviews")
public class AppReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/findall")
    public String getAllReviews(Model model) {
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "reviews"; // return the view name
    }

    // Similar changes for other methods...

    @PostMapping("/uploadReview")
    public String uploadReview(@Valid @ModelAttribute ReviewCreateDto dto, Model model) {
        try {
            Review review = reviewService.create(dto);
            model.addAttribute("review", review);
            return "app/user_view/course/course-preview"; // return the view name
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "app/error/500"; // return the view name
        }
    }

    // Similar changes for other methods...
}
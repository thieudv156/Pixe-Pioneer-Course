package vn.aptech.pixelpioneercourse.controller.app.review;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.aptech.pixelpioneercourse.dto.ReviewCreateDto;
import vn.aptech.pixelpioneercourse.entities.Review;
import vn.aptech.pixelpioneercourse.service.ReviewService;
import vn.aptech.pixelpioneercourse.until.SensitiveWordFilter;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/app/reviews")
public class AppReviewController {
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private ModelMapper mapper;

    @GetMapping("/findall")
    public String getAllReviews(Model model) {
        List<Review> reviews = reviewService.findAll();
        model.addAttribute("reviews", reviews);
        return "reviews"; // return the view name
    }

    // Similar changes for other methods...

    @PostMapping("/uploadReview")
    public String uploadReview(@RequestParam("userId") Integer uid, @RequestParam("courseId") Integer courseId, @RequestParam("content") String content, @RequestParam("rating") Integer rating, Model model, RedirectAttributes ra) {
        try {
        	if (!SensitiveWordFilter.sensitiveWordsChecker(content)) {
        		ra.addFlashAttribute("ErrorCondition", true);
        		ra.addFlashAttribute("ErrorError", "Bad word detected.");
        		return "redirect:/app/course/preview/" + courseId.toString();
        	}
            // Assuming ReviewCreateDto has setters for content and rating
            ReviewCreateDto dto = new ReviewCreateDto();
            dto.setContent(content);
            dto.setRating(rating);
            dto.setCourseId(courseId);
            dto.setUserId(uid);
            dto.setCreatedAt(LocalDateTime.now());
            Review review = reviewService.create(dto); // Implement reviewService.create() method
            model.addAttribute("review", review);
            // Redirect to the course preview page or wherever appropriate
            return "redirect:/app/course/preview/" + review.getCourse().getId().toString(); 
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "app/error/500";
        }
    }

    @PostMapping("/editReview")
    public String editReview(@RequestParam("reviewId") Integer reviewId, @RequestParam("courseId") Integer courseId, @RequestParam("userId") Integer userId, @RequestParam("edited_content") String content, @RequestParam("edited_rating") Integer rating, Model model, RedirectAttributes ra) {
    	try {
    		if (!SensitiveWordFilter.sensitiveWordsChecker(content)) {
        		ra.addFlashAttribute("ErrorCondition", true);
        		ra.addFlashAttribute("ErrorError", "Bad word detected.");
        		return "redirect:/app/course/preview/" + courseId.toString();
        	}
    		ReviewCreateDto rvc = mapper.map(reviewService.findById(reviewId), ReviewCreateDto.class);
    		Review updated = reviewService.update(reviewId, rvc);
    		return "redirect:/app/course/preview/"+ updated.getCourse().getId().toString();
    	} catch (Exception e) {
    		model.addAttribute("error", e.getMessage());
    		return "app/error/500";
    	}
    }
    
    @GetMapping("/deleteReview")
    public String deleteReview(@RequestParam("reviewId") Integer reviewId, @RequestParam("courseId") Integer courseId, Model model) {
    	try {
    		reviewService.delete(reviewId);
    		return "redirect:/app/course/preview/"+courseId.toString();
    	} catch (Exception e) {
    		model.addAttribute("erorr", e.getMessage());
    		return "app/error/500";
    	}
    }
    // Similar changes for other methods...
}
package vn.aptech.pixelpioneercourse.controller.app.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.entities.Review;
import vn.aptech.pixelpioneercourse.service.ReviewService;

@Controller
@RequestMapping("/app/admin/reviews")
public class ReviewManagingController {
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private JavaMailSender mailSender;
	
//	private void sendSimpleMessage(String receiverEmail, String content) {
//        SimpleMailMessage message = new SimpleMailMessage(); 
//        message.setFrom("bot@PIXELPIONEERCOURSE.com");
//        message.setTo(receiverEmail); 
//        message.setSubject("Warnings from Pixel Pioneer Course"); 
//        message.setText(content);
//        mailSender.send(message);
//    }
	
	private void sendHtmlMessage(String to, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("bot@PIXELPIONEERCOURSE.com");
        helper.setTo(to);
        helper.setSubject("Warnings from Pixel Pioneer Course");
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
	
	@GetMapping
	public String reviewPage(Model model, HttpSession session) {
		if (session.getAttribute("isAdmin") != null) {
			List<Review> reviews = reviewService.findAll();
			model.addAttribute("reviews", reviews);
			return "app/admin_view/review/general";
		} else {
			return "redirect:/";
		}
	}
	
	@PostMapping
	public String searchPage(Model model, @RequestParam("query") String query, RedirectAttributes ra) {
		try {
			List<Review> relatedReviews = reviewService.findByQuery(query);
			if (relatedReviews.isEmpty()) {
				ra.addFlashAttribute("ErrorCondition",true);
				ra.addFlashAttribute("ErrorError", "Cannot find such review");
				return "redirect:/app/admin/reviews";
			} else {
				model.addAttribute("reviews", relatedReviews);
				return "app/admin_view/review/general";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/error/500";
		}
	}
	
	@PostMapping("/warn")
	public String sendWarnMessage(@RequestParam("reviewId") String reviewId, @RequestParam("warningContent") String content, RedirectAttributes ra) {
	    try {
	        Integer rid = Integer.parseInt(reviewId);
	        Review review = reviewService.findById(rid);
	        content += "<br><strong>Potential harmful review message from a course named " +
	                "<span style='color: red;'>" + review.getCourse().getTitle() + "</span>:</strong> " + review.getContent();
	        sendHtmlMessage(review.getUser().getEmail(), content);
	        ra.addFlashAttribute("SuccessCondition", true);
	        ra.addFlashAttribute("SuccessSuccess", "Successfully sent warning to user " + review.getUser().getFullName());
	        return "redirect:/app/admin/reviews";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/app/error/500";
	    }
	}
	
	@PostMapping("/delete")
	public String deleteReview(@RequestParam("reviewId") String reviewId, RedirectAttributes ra) {
		try {
			Integer rid = Integer.parseInt(reviewId);
			Review r = reviewService.findById(rid);
			if (r == null) {
				ra.addFlashAttribute("ErrorCondition",true);
				ra.addFlashAttribute("ErrorError", "This review does not exist anymore");
				return "redirect:/app/admin/reviews";
			} else {
				reviewService.delete(rid);
				ra.addFlashAttribute("SuccessCondition",true);
				ra.addFlashAttribute("SuccessSuccess", "Delete review successfully");
				return "redirect:/app/admin/reviews";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/error/500";
		}
	}
}

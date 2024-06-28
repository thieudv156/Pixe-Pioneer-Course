package vn.aptech.pixelpioneercourse.controller.app.admin;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import vn.aptech.pixelpioneercourse.entities.Review;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.service.DiscussionService;

@Controller
@RequestMapping("/app/admin/discussion")
public class DiscussionManagingController {
	
	@Autowired
	private DiscussionService discussionService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@GetMapping
	public String discussionPage(Model model, HttpSession session) {
	    if (session.getAttribute("isAdmin") != null) {
	    	try {
		        List<Discussion> discussions = discussionService.findAll();
		        List<List<String>> discussionsData = new ArrayList<>();

		        for (Discussion discussion : discussions) {
		            List<String> data = new ArrayList<>();
		            data.add(discussion.getUser().getRole().getRoleName());
		            data.add(discussion.getContent());
		            data.add(discussion.getUser().getFullName());
		            data.add(discussion.getUser().getEmail());
		            data.add(discussion.getSubLesson().getTitle());
		            data.add(discussion.getSubLesson().getId().toString());
		            data.add(discussion.getSubLesson().getLesson().getTitle());
		            data.add(discussion.getSubLesson().getLesson().getId().toString());
		            data.add(discussion.getSubLesson().getLesson().getCourse().getTitle());
		            data.add(discussion.getSubLesson().getLesson().getCourse().getId().toString());
		            data.add(discussion.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		            data.add(discussion.getId().toString());
		            discussionsData.add(data);
		        }

		        model.addAttribute("discussionsData", discussionsData);
		        return "app/admin_view/discussion/general";
		    } catch (Exception e) {
		    	e.printStackTrace();
		        return "redirect:/app/error/500";
		    }
	    } else {
	    	return "redirect:/";
	    }
	}
	
	@PostMapping
	public String searchDiscussion(@RequestParam("query") String query, Model model, RedirectAttributes ra) {
		try {
			List<Discussion> relatedDiscussion = discussionService.findByQuery(query);
			if (relatedDiscussion.isEmpty()) {
				ra.addFlashAttribute("ErrorCondition", true);
				ra.addFlashAttribute("ErrorError", "There is no such discussion");
				return "redirect:/app/admin/discussion";
			} else {
				List<List<String>> discussionsData = new ArrayList<>();

		        for (Discussion discussion : relatedDiscussion) {
		            List<String> data = new ArrayList<>();
		            data.add(discussion.getUser().getRole().getRoleName());
		            data.add(discussion.getContent());
		            data.add(discussion.getUser().getFullName());
		            data.add(discussion.getUser().getEmail());
		            data.add(discussion.getSubLesson().getTitle());
		            data.add(discussion.getSubLesson().getId().toString());
		            data.add(discussion.getSubLesson().getLesson().getTitle());
		            data.add(discussion.getSubLesson().getLesson().getId().toString());
		            data.add(discussion.getSubLesson().getLesson().getCourse().getTitle());
		            data.add(discussion.getSubLesson().getLesson().getCourse().getId().toString());
		            data.add(discussion.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		            data.add(discussion.getId().toString());
		            discussionsData.add(data);
		        }
				model.addAttribute("discussionsData", discussionsData);
				return "app/admin_view/discussion/general";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/app/error/500";
		}
	}
	
//	@GetMapping("/update")
//	public String updatePageGet() {
//		return "redirect:/app/admin/discussion";
//	}
//	
//	@PostMapping("/update")
//	public String updatePage(@RequestParam("id") Integer id, Model model, RedirectAttributes ra) {
//		try {
//			Discussion discussion = discussionService.findById(id);
//			model.addAttribute("discussion", discussion);
//			return "app/admin_view/discussion/update";
//		} catch (Exception e) {
//			ra.addFlashAttribute("ErrorCondition", true);
//			ra.addFlashAttribute("ErrorError", e.getMessage());
//			return "redirect:/app/admin/discussion";
//		}
//	}
//	
//	@GetMapping("/update/updateCheck")
//	public String updateCheckPageGet() {
//		return "redirect:/app/admin/discussion";
//	}
//	
//	@PostMapping("/update/updateCheck")
//	public String update(@ModelAttribute("discussion") Discussion discussion, RedirectAttributes ra) {
//		try {
//			Discussion updatedOne = discussionService.findById(discussion.getId());
//			updatedOne.setContent(discussion.getContent());
//			discussionService.updateDiscussion(updatedOne.getId(), mapper.map(updatedOne, DiscussionCreateDto.class));
//			ra.addAttribute("SuccessCondition", true);
//			ra.addAttribute("SuccessSuccess", "Update content successfully");
//			return "redirect:/app/admin/discussion";
//		} catch (Exception e) {
//			return "redirect:/app/admin/discussion";
//		}
//	}
	
	@GetMapping("/delete")
	public String deletePageGet() {
		return "redirect:/app/admin/discussion";
	}
	
	@PostMapping("/delete")
	public String delete(@RequestParam("id") String id, RedirectAttributes ra) {
		try {
			Integer did = Integer.parseInt(id);
			discussionService.deleteById(did);
			ra.addFlashAttribute("SuccessCondition", true);
			ra.addFlashAttribute("SuccessSuccess", "Successfully delete this discussion");
			return "redirect:/app/admin/discussion";
		} catch (Exception e) {
			e.printStackTrace();
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", "Cannot delete this discussion");
			return "redirect:/app/error/500";
		}
	}
	
	private void sendHtmlMessage(String to, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("bot@PIXELPIONEERCOURSE.com");
        helper.setTo(to);
        helper.setSubject("Warnings from Pixel Pioneer Course");
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
	
	@PostMapping("/warn")
	public String sendWarnMessage(@RequestParam("discussionId") String discussionId, @RequestParam("warningContent") String content, RedirectAttributes ra) {
	    try {
	        Integer did = Integer.parseInt(discussionId);
	        Discussion discussion = discussionService.findById(did);
	        content += "<br><strong>Potential harmful discussion message which appears in sublesson named " +
	                "<span style='color: red;'>" + discussion.getSubLesson().getTitle() + "</span>" +
	                " of a course named " +
	                "<span style='color: lightblue;'>" + discussion.getSubLesson().getLesson().getCourse().getTitle() + "</span>:" +
	                "</strong> " + discussion.getContent();
	        sendHtmlMessage(discussion.getUser().getEmail(), content);
	        ra.addFlashAttribute("SuccessCondition", true);
	        ra.addFlashAttribute("SuccessSuccess", "Successfully sent warning to user " + discussion.getUser().getFullName());
	        return "redirect:/app/admin/discussion";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "redirect:/app/error/500";
	    }
	}
}


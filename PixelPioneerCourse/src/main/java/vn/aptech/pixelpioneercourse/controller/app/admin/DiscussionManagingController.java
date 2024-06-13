package vn.aptech.pixelpioneercourse.controller.app.admin;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.service.DiscussionService;

@Controller
@RequestMapping("/app/discussion")
public class DiscussionManagingController {
	private final DiscussionService discussionService;

	public DiscussionManagingController(DiscussionService discussionService, ModelMapper mapper) {
		this.discussionService = discussionService;
        this.mapper = mapper;
    }

	private final ModelMapper mapper;
	
	@GetMapping
	public String discussionPage(Model model, HttpSession session) {
	    if (session.getAttribute("isAdmin") != null) {
	    	try {
		        List<Discussion> discussions = discussionService.findAll();
		        List<List<String>> discussionsData = new ArrayList<>();

		        for (Discussion discussion : discussions) {
		            List<String> data = new ArrayList<>();
		            data.add(String.valueOf(discussion.getId()));
		            data.add(discussion.getContent());
		            data.add(discussion.getUser().getUsername());
		            data.add(discussion.getUser().getEmail());
		            data.add(discussion.getSubLesson().getTitle());
		            data.add(discussion.getSubLesson().getId().toString());
		            data.add(discussion.getSubLesson().getLesson().getTitle());
		            data.add(discussion.getSubLesson().getLesson().getId().toString());
		            data.add(discussion.getSubLesson().getLesson().getCourse().getTitle());
		            data.add(discussion.getSubLesson().getLesson().getCourse().getId().toString());
		            data.add(discussion.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
		            discussionsData.add(data);
		        }

		        model.addAttribute("discussionsData", discussionsData);
		        return "app/admin_view/discussion/general";
		    } catch (Exception e) {
		        return "redirect:/";
		    }
	    } else {
	    	return "redirect:/";
	    }
	}
	
	@GetMapping("/update")
	public String updatePageGet() {
		return "redirect:/app/admin/discussion";
	}
	
	@PostMapping("/update")
	public String updatePage(@RequestParam("id") Integer id, Model model, RedirectAttributes ra) {
		try {
			Discussion discussion = discussionService.findById(id);
			model.addAttribute("discussion", discussion);
			return "app/admin_view/discussion/update";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage());
			return "redirect:/app/admin/discussion";
		}
	}
	
	@GetMapping("/update/updateCheck")
	public String updateCheckPageGet() {
		return "redirect:/app/admin/discussion";
	}
	
	@PostMapping("/update/updateCheck")
	public String update(@ModelAttribute("discussion") Discussion discussion, RedirectAttributes ra) {
		try {
			Discussion updatedOne = discussionService.findById(discussion.getId());
			updatedOne.setContent(discussion.getContent());
			discussionService.updateDiscussion(updatedOne.getId(), mapper.map(updatedOne, DiscussionCreateDto.class));
			ra.addAttribute("SuccessCondition", true);
			ra.addAttribute("SuccessSuccess", "Update content successfully");
			return "redirect:/app/admin/discussion";
		} catch (Exception e) {
			return "redirect:/app/admin/discussion";
		}
	}
	
	@GetMapping("/delete")
	public String deletePageGet() {
		return "redirect:/app/admin/discussion";
	}
	
	@PostMapping("/delete")
	public String delete(@RequestParam("id") Integer id, RedirectAttributes ra) {
		try {
			discussionService.deleteById(id);
			ra.addFlashAttribute("SuccessCondition", true);
			ra.addFlashAttribute("SuccessSuccess", "Successfully delete this discussion");
			return "redirect:/app/admin/discussion";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", "Cannot delete this discussion");
			return "redirect:/app/admin/discussion";
		}
	}
}


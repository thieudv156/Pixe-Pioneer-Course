package vn.aptech.pixelpioneercourse.controller.app.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.aptech.pixelpioneercourse.entities.Discussion;
import vn.aptech.pixelpioneercourse.service.DiscussionService;

@Controller
@RequestMapping("/app/admin/discussion")
public class DiscussionController {
	private final DiscussionService discussionService;

	public DiscussionController(DiscussionService discussionService) {
		this.discussionService = discussionService;
	}
	
	@GetMapping
	public String discussionPage(Model model) {
		try {
			List<Discussion> discussions = discussionService.findAll();
			model.addAttribute("discussions", discussions);
			return "app/admin_view/discussion/general";
		} catch (Exception e) {
			return "redirect:/app/course";
		}
	}
}


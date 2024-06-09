package vn.aptech.pixelpioneercourse.controller.app.sublession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.aptech.pixelpioneercourse.dto.DiscussionCreateDto;
import vn.aptech.pixelpioneercourse.entities.Discussion;
import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.service.DiscussionService;

@Controller
@RequestMapping("/app/sublesson")
public class SublessonController {
	@Autowired
	private DiscussionService discussionService;
	
	@GetMapping("/discussions")
	public List<Discussion> getRelatedDiscussionForSublessonPages(SubLesson sublesson, Model model) {
		List<Discussion> related = discussionService.findBySubLessonId(sublesson.getId());
		model.addAttribute("discussions", related);
		return related;
	}
	
	@GetMapping("/discussions/{id}")
	public Discussion getRelatedDiscussionWithID(@PathVariable("id") int id, Model model) {
		Discussion d = discussionService.findById(id);
		model.addAttribute("discussionWithID", d);
		return d;
	}
	
	/*
	 * Use RedirectAttributes to send error or success messages to users
	 */
	
	@PostMapping("/discussions/create")
	public String postDiscussion(@RequestBody DiscussionCreateDto dto, RedirectAttributes ra) {
		discussionService.createDiscussion(dto);
		return "redirect:/app/sublesson";
	}
	
	@PostMapping("/discussions/update/{id}")
	public String editDiscussion(@PathVariable("id") int id, @RequestBody DiscussionCreateDto dto, RedirectAttributes ra) {
		try {
//			Discussion d = discussionService.findByDate(dto.getCreatedAt());
			discussionService.updateDiscussion(id, dto);
			return "redirect:/app/sublesson";
		} catch (Exception e) {
			return "redirect:/app/sublesson";
		}
	}
	
	@PostMapping("/discussion/delete/{id}")
	public String deleteDiscussion(@PathVariable("id") int id, RedirectAttributes ra) {
		try {
			discussionService.deleteById(id);
			return "redirect:/app/sublesson";
		} catch (Exception e) {
			return "redirect:/app/sublesson";
		}
	}
}

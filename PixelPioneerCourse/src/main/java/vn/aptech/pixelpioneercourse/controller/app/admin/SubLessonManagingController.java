package vn.aptech.pixelpioneercourse.controller.app.admin;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import vn.aptech.pixelpioneercourse.entities.SubLesson;
import vn.aptech.pixelpioneercourse.service.SubLessonService;

@Controller
@RequestMapping("/app/admin/sublesson")
public class SubLessonManagingController {
	@Autowired
	private SubLessonService sublessonService;
	
	@GetMapping
	public String sublessonPage(Model model) {
		try {
	        List<SubLesson> sublessons = sublessonService.findAllSubLessons();
	        List<List<String>> sublessonsData = new ArrayList<>();

	        for (SubLesson sublesson : sublessons) {
	        	List<String> data = new ArrayList<>();
	            data.add(sublesson.getId().toString());
	            data.add(sublesson.getTitle());
	            data.add(sublesson.getContent());
	            data.add(sublesson.getLesson().getTitle());
	            data.add(sublesson.getLesson().getId().toString());
	            data.add(sublesson.getFrontPageImage().getImageUrl());
	            data.add(sublesson.getLesson().getCourse().getInstructor().getEmail());
	            data.add(sublesson.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
	            sublessonsData.add(data);
	        }
	        model.addAttribute("sublessons", sublessonsData);
	        return "app/admin_view/sublesson/general";
	    } catch (Exception e) {
	        return "redirect:/";
	    }
	}
	
	@GetMapping("/delete")
	public String deletePageGet() {
		return "redirect:/app/admin/sublesson";
	}
	
	@PostMapping("/delete")
	public String delete(@RequestParam("id") Integer id, RedirectAttributes ra) {
		try {
			sublessonService.delete(id);
			ra.addFlashAttribute("SuccessCondition", true);
			ra.addFlashAttribute("SuccessSuccess", "Delete sublesson successfully");
			return "redirect:/app/admin/sublesson";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", "Unable to delete sublesson");
			return "redirect:/app/admin/sublesson";
		}
	}
}

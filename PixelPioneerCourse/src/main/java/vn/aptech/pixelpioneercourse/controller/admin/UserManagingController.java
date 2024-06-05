package vn.aptech.pixelpioneercourse.controller.admin;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/admin/users")
public class UserManagingController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	
//	@GetMapping
//	public String adminIndex(HttpSession session) {
//		if (session.getAttribute("isAdmin") != null) {
//			return "admin_view/users/general";
//		} else {
//			return "redirect:/app/register";
//		}
//	}
	
	@GetMapping
	public String adminIndex(Model model, HttpSession session) {
	    if (session.getAttribute("isAdmin") != null) {
	        List<User> users = userService.findAll();
	        model.addAttribute("users", users);
	        return "admin_view/users/general";
	    } else {
	        return "redirect:/app/course";
	    }
	}

	@PostMapping("/deactivate")
	public String deactivation(@RequestParam("uid") Integer uid, RedirectAttributes ra) {
		try {
			User u = userService.findById(uid);
			if (u != null) {
				u.setActiveStatus(!u.isActiveStatus());
				userService.updateWithRole(u, uid);
				return "redirect:/app/admin/users";
			} else {
				ra.addFlashAttribute("ErrorCondition", true);
				ra.addFlashAttribute("ErrorError", "Unknown error occurs.");
				return "redirect:/app/admin/users";
			}
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage());
			return "redirect:/app/admin/users";
		}
	}
	
	@PostMapping("/activate")
	public String activation(@RequestParam("uid") int uid, RedirectAttributes ra) {
		try {
			User u = userService.findById(uid);
			if (u != null) {
				u.setActiveStatus(!u.isActiveStatus());
				userService.updateWithRole(u, uid);
				return "redirect:/app/admin/users";
			} else {
				ra.addFlashAttribute("ErrorCondition", true);
				ra.addFlashAttribute("ErrorError", "Unknown error occurs.");
				return "redirect:/app/admin/users";
			}
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage());
			return "redirect:/app/admin/users";
		}
	}
}

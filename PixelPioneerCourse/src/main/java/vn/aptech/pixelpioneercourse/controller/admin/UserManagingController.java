package vn.aptech.pixelpioneercourse.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/admin/users")
public class UserManagingController {
	
	@Autowired
	private UserService userService;
	
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
	        return "redirect:/app/login";
	    }
	}

}

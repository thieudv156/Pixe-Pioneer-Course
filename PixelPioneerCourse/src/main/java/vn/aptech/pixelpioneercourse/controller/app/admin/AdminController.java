package vn.aptech.pixelpioneercourse.controller.app.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/adminDirector")
public class AdminController {

	@GetMapping
	public String index() {
		try {
			return "redirect:/app/admin/dashboard";
		} catch (Exception e) {
			return "redirect:/";
		}
	}
	
}

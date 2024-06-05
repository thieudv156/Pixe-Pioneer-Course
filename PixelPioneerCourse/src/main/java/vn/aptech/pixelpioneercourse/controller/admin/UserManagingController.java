package vn.aptech.pixelpioneercourse.controller.admin;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.RoleDto;
import vn.aptech.pixelpioneercourse.entities.Role;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.RoleRepository;
import vn.aptech.pixelpioneercourse.service.RoleService;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/admin/users")
public class UserManagingController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleRepository roleRepository;
	
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
	
	@PostMapping("/update")
	public String updatePage(@RequestParam("id") String id, RedirectAttributes ra, Model model) {
		try {
			int uid = Integer.parseInt(id);
			User u = userService.findById(uid);
			List<RoleDto> allRoles = roleService.findAll();
			model.addAttribute("allRoles", allRoles);
			model.addAttribute("userInfo", u);
			model.addAttribute("uid", uid);
			return "admin_view/users/update";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage());
			return "redirect:/app/admin/users";
		}
	}
	
	@PostMapping("/update/updateCheck")
	public String update(@ModelAttribute("userInfo") User user, RedirectAttributes ra) {
		try {
			if (user.getPassword().indexOf("$") != 0) {
				user.setPassword(encoder.encode(user.getPassword()));
			}
			Role role = mapper.map(roleService.findById(user.getRole().getId()).get(), Role.class);
			System.out.println(user.getId());
			user.setRole(role);
			userService.updateWithRole(user, user.getId());
			ra.addFlashAttribute("SuccessCondition", true);
			ra.addFlashAttribute("SuccessSuccess", "Update user successfully");
			return "redirect:/app/admin/users";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage());
			return "redirect:/app/admin/users";
		}
	}
}

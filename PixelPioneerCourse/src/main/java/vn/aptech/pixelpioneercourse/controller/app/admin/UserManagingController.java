package vn.aptech.pixelpioneercourse.controller.app.admin;

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
	    try {
	    	if (session.getAttribute("isAdmin") != null) {
		        List<User> users = userService.findAll();
		        model.addAttribute("users", users);
		        return "app/admin_view/users/general";
		    } else {
		        return "redirect:/";
		    }
	    } catch (Exception e) {
	    	return "redirect:/logout";
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
	
	@GetMapping("/create")
	public String createPage(HttpSession session) {
		if (session.getAttribute("isAdmin") != null) {
			return "app/admin_view/users/create";
		}
		return "redirect:/";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute("userInfo") User user, RedirectAttributes ra) {
		try {
			userService.findByEmail(user.getEmail());
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", "User has existed with email "+user.getEmail());
			return "redirect:/app/admin/users/create";
		} catch (Exception e) {
			try {
				userService.findByUsername(user.getUsername());
				ra.addFlashAttribute("ErrorCondition", true);
				ra.addFlashAttribute("ErrorError", "User has existed with username "+user.getUsername());
				return "redirect:/app/admin/users/create";
			} catch (Exception e2) {
				userService.create(user);
				ra.addFlashAttribute("SuccessCondition", true);
				ra.addFlashAttribute("SuccessSuccess", "Successfully create user.");
				return "redirect:/app/admin/users";
			}
		}
	}
	
	@GetMapping("/update") //prevent error
	public String updatePage(HttpSession session) {
		if (session.getAttribute("isAdmin") != null) {
			return "redirect:/app/admin/users";
		}
		return "redirect:/";
	}
	
	@PostMapping("/update")
	public String updatePage(@RequestParam("id") String id, RedirectAttributes ra, Model model, HttpSession session) {
		try {
			if (session.getAttribute("isAdmin") != null) {
				int uid = Integer.parseInt(id);
				User u = userService.findById(uid);
				List<RoleDto> allRoles = roleService.findAll();
				model.addAttribute("allRoles", allRoles);
				model.addAttribute("userInfo", u);
				model.addAttribute("uid", uid);
				return "app/admin_view/users/update";
			}
			return "redirect:/";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage());
			return "redirect:/app/admin/users";
		}
	}
	
	@PostMapping("/update/updateCheck")
	public String update(@ModelAttribute("userInfo") User user, RedirectAttributes ra) {
		try {
			Role role = mapper.map(roleService.findById(user.getRole().getId()).get(), Role.class);
			System.out.println(user.getId());
			userService.updateAdmin(role, user.getId());
			ra.addFlashAttribute("SuccessCondition", true);
			ra.addFlashAttribute("SuccessSuccess", "Update user successfully");
			return "redirect:/app/admin/users";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage());
			return "redirect:/app/admin/users";
		}
	}
	
	@GetMapping("/delete")
	public String deletePage(HttpSession session) {
		if (session.getAttribute("isAdmin") != null) {
			return "redirect:/app/admin/users";
		}
		return "redirect:/";
	}
	
	@PostMapping("/delete")
	public String delete(@ModelAttribute("uid") String uid, RedirectAttributes ra, HttpSession session) {
		try {
			if (session.getAttribute("isAdmin") != null) {
				userService.delete(userService.findById(Integer.parseInt(uid)));
				ra.addFlashAttribute("SuccessCondition", true);
				ra.addFlashAttribute("SuccessSuccess", "Successfully delete user.");
				return "redirect:/app/admin/users";
			}
			return "redirect:/";
		} catch (Exception e) {
			ra.addFlashAttribute("ErrorCondition", true);
			ra.addFlashAttribute("ErrorError", e.getMessage()+".");
			return "redirect:/app/admin/users";
		}
	}
}

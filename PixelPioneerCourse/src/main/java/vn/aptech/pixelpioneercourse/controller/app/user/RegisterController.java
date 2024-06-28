package vn.aptech.pixelpioneercourse.controller.app.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.UserCreateDto;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/register")
public class RegisterController {

    private final UserService userService;

    @Autowired
    private ModelMapper mapper;

    public RegisterController(UserService uService) {
        userService = uService;
    }

    @GetMapping
    public String registerPage(HttpSession session) {
        if (session.getAttribute("isUser") != null || session.getAttribute("isAdmin") != null || session.getAttribute("isInstructor") != null) {
            // Redirect based on the role
            if (session.getAttribute("isUser") != null) {
                return "redirect:/app/course";
            } else if (session.getAttribute("isAdmin") != null) {
                return "redirect:/app/admin";
            } else if (session.getAttribute("isInstructor") != null) {
                return "redirect:/app/course";
            }
        }
        return "app/guest_view/register";
    }

    @PostMapping
    public String register(@ModelAttribute UserCreateDto dto, RedirectAttributes ra) {
        try {
        	try {
	        	int textPhone = Integer.parseInt(dto.getPhone());
	        	if (dto.getPhone().length() < 9 || dto.getPhone().length() > 11) {
	        		ra.addFlashAttribute("failRegisterationCondition", true);
	        		ra.addFlashAttribute("failRegisteration", "Phone should only contain 9 to 11 digits");
	        		return "redirect:/app/register";
	        	}
	        	if (dto.getUsername().contains(" ")) {
	        		ra.addFlashAttribute("failRegisterationCondition", true);
	        		ra.addFlashAttribute("failRegisteration", "Username should not contain spaces");
	        		return "redirect:/app/register";
	        	}
	        } catch (Exception e) {
	        	ra.addFlashAttribute("failRegisterationCondition", true);
	        	ra.addFlashAttribute("failRegisteration", "Phone should contain only digits");
	        	return "redirect:/app/register";
	        }
            if (userService.create(dto)) {
                ra.addFlashAttribute("successCondition", true);
                ra.addFlashAttribute("successMessage", "Successful registration, please log in to your account");
                return "redirect:/app/login";
            } else {
                ra.addFlashAttribute("failRegisterationCondition", true);
                ra.addFlashAttribute("failRegisteration", "Invalid information or there are accounts already exist with such information. Please try a different username/email/phone");
                return "redirect:/app/register";
            }
        } catch (Exception e) {
            ra.addFlashAttribute("failRegisterationCondition", true);
            ra.addFlashAttribute("failRegisteration", e.getMessage());
            return "redirect:/app/register";
        }
    }
}

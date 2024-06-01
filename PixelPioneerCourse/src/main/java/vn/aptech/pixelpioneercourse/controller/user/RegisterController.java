package vn.aptech.pixelpioneercourse.controller.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public String registerPage() {
		return "guest_view/register";
	}
	
	@PostMapping
	public String register(@ModelAttribute UserCreateDto dto, RedirectAttributes redirectAttributes) {
	    try {
	    	if (userService.create(dto)) {
		        redirectAttributes.addFlashAttribute("successCondition", true);
		        redirectAttributes.addFlashAttribute("successMessage", "Successful registeration, please log in to your account");
		        return "redirect:/app/login";
		    } else {
		        redirectAttributes.addFlashAttribute("failRegisterationCondition", true);
		        redirectAttributes.addFlashAttribute("failRegisteration", "Invalid information or account has already existed");
		        return "redirect:/app/register";
		    }
	    } catch (Exception e) {
	    	redirectAttributes.addFlashAttribute("failRegisterationCondition", true);
	        redirectAttributes.addFlashAttribute("failRegisteration", e.getMessage());
	        return "redirect:/app/register";
	    }
	}

}

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
    public String register(@ModelAttribute UserCreateDto dto, RedirectAttributes redirectAttributes) {
        try {
            if (userService.create(dto)) {
                redirectAttributes.addFlashAttribute("successCondition", true);
                redirectAttributes.addFlashAttribute("successMessage", "Successful registration, please log in to your account");
                return "redirect:/app/login";
            } else {
                redirectAttributes.addFlashAttribute("failRegistrationCondition", true);
                redirectAttributes.addFlashAttribute("failRegistration", "Invalid information or account has already existed");
                return "redirect:/app/register";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("failRegistrationCondition", true);
            redirectAttributes.addFlashAttribute("failRegistration", e.getMessage());
            return "redirect:/app/register";
        }
    }
}

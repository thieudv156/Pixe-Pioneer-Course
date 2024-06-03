package vn.aptech.pixelpioneercourse.controller.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.CustomOauth2User;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/login")
public class LoginController {

    private final UserService userService;

    @Autowired
    private ModelMapper mapper;

    public LoginController(UserService uService) {
        userService = uService;
    }

    @GetMapping
    public String loginPage(HttpSession session) {
    	System.out.println(session.getAttribute("isUser"));
    	System.out.println(session.getAttribute("isAdmin"));
    	System.out.println(session.getAttribute("isInstructor"));
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
        return "guest_view/login";
    }
    
    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal CustomOauth2User customUser) {
        // Use customUser here
        return "redirect:/app/course";
    }

    @PostMapping
    public String checkLogin(@RequestParam("info") String emailorusername, @RequestParam("password") String password, RedirectAttributes redirectAttributes, HttpSession session) {
        String role = userService.checkLogin(emailorusername, password);
        if (role.equals("")) {
            redirectAttributes.addFlashAttribute("loginErrorCondition", true);
            redirectAttributes.addFlashAttribute("loginError", "Invalid username/email or password");
            return "redirect:/app/login";
        } else if (role.equals("ROLE_USER")) {
            session.setAttribute("isUser", true);
            session.setAttribute("isAdmin", null);
            session.setAttribute("isInstructor", null);
            return "redirect:/app/course";
        } else if (role.equals("ROLE_ADMIN")) {
            session.setAttribute("isAdmin", true);
            session.setAttribute("isUser", null);
            session.setAttribute("isInstructor", null);
            return "redirect:/app/admin";
        } else if (role.equals("ROLE_INSTRUCTOR")) {
            session.setAttribute("isInstructor", true);
            session.setAttribute("isAdmin", null);
            session.setAttribute("isUser", null);
            return "redirect:/app/course";
        }
        redirectAttributes.addFlashAttribute("loginErrorCondition", true);
        redirectAttributes.addFlashAttribute("loginError", "Unknown error occurs");
        return "redirect:/app/login";
    }
}

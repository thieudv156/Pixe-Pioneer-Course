package vn.aptech.pixelpioneercourse.controller.app.user;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.EnrollmentService;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/login")
public class LoginController {

    private final UserService userService;
    private final EnrollmentService enrollmentService;

    @Autowired
    private ModelMapper mapper;

    public LoginController(UserService uService, EnrollmentService eService) {
        userService = uService;
        enrollmentService = eService;
    }

    @GetMapping
    public String loginPage(HttpSession session) {
        if (session.getAttribute("isUser") != null || session.getAttribute("isAdmin") != null || session.getAttribute("isInstructor") != null) {
            // Redirect based on the role
            if (session.getAttribute("isUser") != null) {
                return "redirect:/";
            } else if (session.getAttribute("isAdmin") != null) {
                return "redirect:/app/admin";
            } else if (session.getAttribute("isInstructor") != null) {
                return "redirect:/app/course/instructor/";
            }
        }
        return "app/guest_view/login";
    }
    
    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        // Use customUser here
        return "redirect:/";
    }

    @PostMapping("/checkLogin")
    public String checkLogin(@RequestParam("info") String emailorusername, @RequestParam("password") String password, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
        	User u = userService.checkLogin(emailorusername, password);
        	Enrollment e = null;
        	try {
        		e = enrollmentService.findByUserId(u.getId());
        	} catch (Exception ex) {
        		try {
            		e = enrollmentService.findEnrollmentsByUserID(u.getId()).getLast();
        		}
        		catch (Exception ex2) {
        			if (u.getRole() == null || u.getRole().getRoleName() == null) {
                        redirectAttributes.addFlashAttribute("loginErrorCondition", true);
                        redirectAttributes.addFlashAttribute("loginError", "Incorrect username/email or password");
                        return "redirect:/app/login";
                    } else if (u.getRole().getRoleName().equals("ROLE_USER") && session.getAttribute("isUser") == null) {
                    	session.setAttribute("user", u);
                    	session.setAttribute("userId", u.getId());
                    	session.setAttribute("enrollment", e);
                        session.setAttribute("isUser", true);
                        session.setAttribute("isAdmin", null);
                        session.setAttribute("isInstructor", null);
                        return "redirect:/app/login/loginSuccess";
                    } else if (u.getRole().getRoleName().equals("ROLE_ADMIN") && session.getAttribute("isAdmin") == null) {
                        session.setAttribute("isAdmin", true);
                        session.setAttribute("userId", u.getId());
                        session.setAttribute("user", u);
                        session.setAttribute("enrollment", e);
                        session.setAttribute("isUser", null);
                        session.setAttribute("isInstructor", null);
                        return "redirect:/app/admin/users";
                    } else if (u.getRole().getRoleName().equals("ROLE_INSTRUCTOR") && session.getAttribute("isInstructor") == null) {
                    	session.setAttribute("user", u);
                    	session.setAttribute("userId", u.getId());
                        session.setAttribute("isInstructor", true);
                        session.setAttribute("enrollment", e);
                        session.setAttribute("isAdmin", null);
                        session.setAttribute("isUser", null);
                        return "redirect:/app/login/loginSuccess";
                    } else {
                    	redirectAttributes.addFlashAttribute("loginErrorCondition", true);
                        redirectAttributes.addFlashAttribute("loginError", "Unknown error occurs");
                        return "redirect:/app/login";
                    }
        		}
        		
        	}
            if (u.getRole() == null || u.getRole().getRoleName() == null) {
                redirectAttributes.addFlashAttribute("loginErrorCondition", true);
                redirectAttributes.addFlashAttribute("loginError", "Incorrect username/email or password");
                return "redirect:/app/login";
            } else if (u.getRole().getRoleName().equals("ROLE_USER") && session.getAttribute("isUser") == null) {
            	session.setAttribute("user", u);
            	session.setAttribute("userId", u.getId());
            	session.setAttribute("enrollment", e);
                session.setAttribute("isUser", true);
                session.setAttribute("isAdmin", null);
                session.setAttribute("isInstructor", null);
                return "redirect:/app/login/loginSuccess";
            } else if (u.getRole().getRoleName().equals("ROLE_ADMIN") && session.getAttribute("isAdmin") == null) {
                session.setAttribute("isAdmin", true);
                session.setAttribute("userId", u.getId());
                session.setAttribute("user", u);
                session.setAttribute("enrollment", e);
                session.setAttribute("isUser", null);
                session.setAttribute("isInstructor", null);
                return "redirect:/app/admin/users";
            } else if (u.getRole().getRoleName().equals("ROLE_INSTRUCTOR") && session.getAttribute("isInstructor") == null) {
            	session.setAttribute("user", u);
            	session.setAttribute("userId", u.getId());
                session.setAttribute("isInstructor", true);
                session.setAttribute("enrollment", e);
                session.setAttribute("isAdmin", null);
                session.setAttribute("isUser", null);
                return "redirect:/app/login/loginSuccess";
            } else {
            	redirectAttributes.addFlashAttribute("loginErrorCondition", true);
                redirectAttributes.addFlashAttribute("loginError", "Unknown error occurs");
                return "redirect:/app/login";
            }
        } catch (Exception e) {
        	redirectAttributes.addFlashAttribute("loginErrorCondition", true);
            redirectAttributes.addFlashAttribute("loginError", e.getMessage());
            return "redirect:/app/login";
        }
    }
}

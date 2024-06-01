package vn.aptech.pixelpioneercourse.controller.user;

import java.util.InputMismatchException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.UserService;

@Controller
@RequestMapping("/app/reset-password")
public class ResetPasswordController {
	
	@Autowired
	private UserService userService;
    
    @Autowired
    private ModelMapper mapper;
    
    @Autowired
    private JavaMailSender mailSender;
    
    String requestedCode;

    public ResetPasswordController(UserService uService) {
        userService = uService;
        requestedCode = userService.codeGeneratorForEmailVerification();
    }
	
    @GetMapping
    public String resetPassPage(HttpSession session) {
    	session.setAttribute("emailVerified", false);
        session.setAttribute("codeVerified", false);
        session.setAttribute("passwordChanged", false);
    	return "guest_view/reset_password";
    }
    
    String requestedEmail = null;
    
    public void sendSimpleMessage(String receiverEmail) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("bot@PIXELPIONEERCOURSE.com");
        message.setTo(receiverEmail); 
        message.setSubject("Email Verification - Code Verifying"); 
        message.setText("Dear " + receiverEmail + ",\n\n" +
                "Thank you for being our member. To complete your password changing process, please enter the following verification code:\n\n" +
                "**" + requestedCode + "**\n\n" +
                "If you did not request this code, please ignore this email or contact support.\n\n" +
                "Best regards,\n" +
                "Pixel Pioneer Course Adminstration Team");
        mailSender.send(message);
    }

    @PostMapping("/mail")
    public String emailChecker(@RequestParam("email") String email, RedirectAttributes ra, HttpSession session) {
        User u = userService.findByEmail(email);
        if (u != null) {
            requestedEmail = email;
            sendSimpleMessage(email);
            session.setAttribute("emailVerified", true); // Set this attribute when email is verified
        } else {
            ra.addFlashAttribute("emailNotFoundCondition", true);
        }
        return "redirect:/app/reset-password";
    }

    @PostMapping("/code")
    public String codeChecker(@RequestParam("codeChecker") String code, RedirectAttributes ra, HttpSession session) {
        if (requestedCode.equals(code)) {
            session.setAttribute("codeVerified", true);
            session.setAttribute("passwordChanged", false); // Reset password change
        } else {
            ra.addFlashAttribute("codeCheckingFailCondition", true);
        }
        return "redirect:/app/reset-password";
    }

    @PutMapping("/password")
    public String passwordChange(@RequestParam("passwordChanger") String pw, RedirectAttributes ra, HttpSession session) {
        try {
        	if (userService.findUserByPassword(pw) == null) {
                userService.passwordChanger(requestedEmail, pw);
                session.setAttribute("passwordChanged", true);
                session.setAttribute("emailVerified", false); // Reset email verification
                session.setAttribute("codeVerified", false); // Reset code verification
                ra.addFlashAttribute("successCondition", true);
                ra.addFlashAttribute("successMessage", "Successfully changed password, please login.");
                return "redirect:/app/login";
            } else {
                ra.addFlashAttribute("loginErrorCondition", true);
                ra.addFlashAttribute("loginError", "Password change failed.");
                return "redirect:/app/reset-password";
            }
        } catch(Exception e) {
        	ra.addFlashAttribute("loginErrorCondition", true);
        	ra.addFlashAttribute("loginError", "Unknown error occurs, please contact us for further support");
        	return "redirect:/app/reset-password";
        }
    }
}

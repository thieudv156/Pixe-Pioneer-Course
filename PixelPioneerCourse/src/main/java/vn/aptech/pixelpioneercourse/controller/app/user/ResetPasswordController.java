package vn.aptech.pixelpioneercourse.controller.app.user;

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
    
    private String requestedEmail = null;
    
    public void sendSimpleMessage(String receiverEmail) {
        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("bot@PIXELPIONEERCOURSE.com");
        message.setTo(receiverEmail); 
        message.setSubject("Email Verification - Code Verifying Letter from PIXEL PIONEER COURSE"); 
        message.setText("Dear " + receiverEmail + ",\n\n" +
                "Thank you for being our member. To complete your password changing process, please enter the following verification code:\n\n" +
                "**" + requestedCode + "**\n\n" +
                "This code will be unavailable right after you correctly enter it." + "\n" +
                "If you did not request this code, please ignore this email or contact support.\n\n" +
                "Best regards,\n" +
                "Pixel Pioneer Course Adminstration Team.");
        mailSender.send(message);
    }
	
    private boolean mailSectionShow = true;
    private boolean codeSectionShow = false;
    private boolean passwordSectionShow = false;
    
    @GetMapping
    public String resetPassPage(HttpSession session) {
    	mailSectionShow = true;
    	session.setAttribute("emailShow", mailSectionShow);
        session.setAttribute("codeShow", codeSectionShow);
        session.setAttribute("passwordShow", passwordSectionShow);
    	return "app/guest_view/reset_password";
    }

    @PostMapping("/mail")
    public String emailChecker(@RequestParam("email") String email, RedirectAttributes ra, HttpSession session) {
    	try {
    		userService.findByEmail(email);
    		requestedEmail = email;
            sendSimpleMessage(email);
            mailSectionShow = false;
            session.setAttribute("emailShow", mailSectionShow); // Set this attribute when email is verified
            codeSectionShow = true;
            session.setAttribute("codeShow", codeSectionShow);
            return "redirect:/app/reset-password";
    	} catch (Exception e) {
    		ra.addFlashAttribute("emailNotFoundCondition", true);
    		ra.addFlashAttribute("emailNotFound", "Cannot find that account in our system :(");
            return "redirect:/app/reset-password";
    	}
        
    }

    @PostMapping("/code")
    public String codeChecker(@RequestParam("codeChecker") String code, RedirectAttributes ra, HttpSession session) {
        if (requestedCode.equals(code)) {
        	codeSectionShow = false;
        	session.setAttribute("codeShow", codeSectionShow);
        	passwordSectionShow = true;
            session.setAttribute("passwordShow", passwordSectionShow);
            requestedCode = null;
        } else {
            ra.addFlashAttribute("codeCheckingFailCondition", true);
            ra.addFlashAttribute("codeCheckingFail", "Wrong code, please enter again.");
        }
        return "redirect:/app/reset-password";
    }

    @PostMapping("/password")
    public String passwordChange(@RequestParam("passwordChanger") String pw, @RequestParam("repasswordChanger") String rpw,RedirectAttributes ra, HttpSession session) {
        try {
        	if (pw.equals(rpw) && (pw.length() > 5 && rpw.length() > 5)) {
        		try {
        			userService.findUserByPassword(pw);
        			ra.addFlashAttribute("loginErrorCondition", true);
                    ra.addFlashAttribute("loginError", "Password change failed (you may enter an old password), please enter a new one.");
                    return "redirect:/app/reset-password";
        		} catch (Exception e) {
        			userService.passwordChanger(requestedEmail, pw);
                    mailSectionShow = true;
                    session.setAttribute("emailShow", mailSectionShow); // Reset email verification
                    codeSectionShow = false;
                    session.setAttribute("codeShow", codeSectionShow); // Reset code verification
                    passwordSectionShow = false;
                    session.setAttribute("passwordShow", passwordSectionShow);
                    requestedEmail = null;
                    ra.addFlashAttribute("successCondition", true);
                    ra.addFlashAttribute("successMessage", "Successfully changed password, please login.");
                    return "redirect:/app/login";
        		}
        	} else if (pw.length() < 6) {
        		ra.addFlashAttribute("loginErrorCondition", true);
        		ra.addFlashAttribute("loginError", "Password must contain at least 6 letters");
        		return "redirect:/app/reset-password";
        	} else {
        		ra.addFlashAttribute("loginErrorCondition", true);
        		ra.addFlashAttribute("loginError", "Passwords don't match each other, please re-type them.");
        		return "redirect:/app/reset-password";
        	}
        } catch(Exception e) {
        	ra.addFlashAttribute("loginErrorCondition", true);
        	ra.addFlashAttribute("loginError", e.getMessage());
        	mailSectionShow = true;
            session.setAttribute("emailShow", mailSectionShow);
            codeSectionShow = false;
            session.setAttribute("codeShow", codeSectionShow);
            passwordSectionShow = false;
            session.setAttribute("passwordShow", passwordSectionShow);
        	return "redirect:/app/reset-password";
        }
    }
    
    @PostMapping("/cancel")
    public String cancelRequest(HttpSession session) {
        // Reset all sections to initial state
        mailSectionShow = true;
        codeSectionShow = false;
        passwordSectionShow = false;

        // Update session attributes
        session.setAttribute("emailShow", mailSectionShow);
        session.setAttribute("codeShow", codeSectionShow);
        session.setAttribute("passwordShow", passwordSectionShow);

        return "redirect:/app/reset-password";
    }
}

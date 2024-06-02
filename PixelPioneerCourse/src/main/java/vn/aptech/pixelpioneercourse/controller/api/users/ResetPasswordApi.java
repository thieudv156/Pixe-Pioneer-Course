package vn.aptech.pixelpioneercourse.controller.api.users;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.aptech.pixelpioneercourse.service.UserService;

@RestController
@RequestMapping("/api/reset-password")
public class ResetPasswordApi {

	@Autowired
	private UserService userService;
	
	@Autowired
    private ModelMapper mapper;
    
    @Autowired
    private JavaMailSender mailSender;
    
    String requestedCode = null;
    
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
    
    private String requestedEmail = null;
    
    @PostMapping("/mail")
    public ResponseEntity<String> checkEmail(@RequestParam("email") String email) {
    	try {
    		userService.findByEmail(email);
    		requestedCode = userService.codeGeneratorForEmailVerification();
    		requestedEmail = email;
    		sendSimpleMessage(email);
    		return ResponseEntity.ok("Successfully sent mail to existed account");
    	} catch (Exception e) {
    		return ResponseEntity.badRequest().body("Account not exist");
    	}
    }
    
    @PostMapping("/code")
    public ResponseEntity<String> checkCode(@RequestParam("code") String code) {
    	try {
    		if (requestedEmail != null) {
        		if (code.equals(requestedCode)) {
            		return ResponseEntity.ok("Verified for password reset");
            	} else {
            		return ResponseEntity.badRequest().body("Incorrect code");
            	}
        	} else {
        		return ResponseEntity.badRequest().body("You are not authorized for this section, please return to other pages :(");
        	}
    	} catch (Exception e) {
    		return ResponseEntity.badRequest().body("Form field is not supported, please contact us for further support");
    	}
    }
    
    @PutMapping("/password")
    public ResponseEntity<String> checkPassword(@RequestParam("newPassword") String np, @RequestParam("renewPassword") String rnp) {
    	try {
    		if (requestedCode != null) {
        		try {
            		if (np.length() < 6 || rnp.length() < 6) {
            			return ResponseEntity.badRequest().body("Password must contain at least 6 letters");
            		}
            		if (!np.equals(rnp)) {
            			return ResponseEntity.badRequest().body("Passwords don't match each other");
            		}
            		if (userService.findUserByPassword(np) == null) {
            			userService.passwordChanger(requestedEmail, np);
            			requestedEmail = null;
            			return ResponseEntity.ok().body("Successfully reset password");
            		} else {
            			return ResponseEntity.badRequest().body("You may have used an old password, please try a new one.");
            		}
            	} catch (Exception e) {
            		try {
            			userService.passwordChanger(requestedEmail, np);
            			requestedEmail = null;
            			return ResponseEntity.ok("Successfully reset password");
            		} catch (Exception e1) {
            			System.out.println(e1.getMessage());
                		return ResponseEntity.badRequest().body("Unknown error occurs, please contact us for further support");
            		}
            	}
        	} else {
        		return ResponseEntity.badRequest().body("You are not authorized for this section, please return to other pages :(");
        	}
    	}
    	catch (Exception e) {
    		return ResponseEntity.badRequest().body("Form field is invalid, please contact us for further support");
    	}
    }
}

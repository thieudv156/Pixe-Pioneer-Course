package vn.aptech.pixelpioneercourse.controller.api.users;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.UserService;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/reset-password")
public class ResetPasswordApi {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordApi.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JavaMailSender mailSender;

    private String requestedCode = null;;
    private String requestedEmail = null;
    private Boolean verified = false;

    @PostMapping("/mail")
    public ResponseEntity<String> checkEmail(@RequestParam("email") String email, HttpSession session) {
        try {
            User u = userService.findByEmail(email);
            requestedCode = userService.codeGeneratorForEmailVerification();
            requestedEmail = u.getEmail();
            sendSimpleMessage(email, requestedCode);
            logger.info("Email and code set in session: {}, {}", email, requestedCode);
            return ResponseEntity.ok("Successfully sent mail to existed account");
        } catch (Exception e) {
            logger.error("Account not exist", e);
            return ResponseEntity.badRequest().body("Account not exist");
        }
    }

    @PostMapping("/code")
    public ResponseEntity<String> checkCode(@RequestParam("code") String code, HttpSession session) {
        try {
            logger.info("Retrieved from session: email={}, code={}", requestedEmail, requestedCode);
            if (requestedEmail != null && requestedCode != null) {
                if (code.equals(requestedCode)) {
                    verified = true;
                    requestedCode = null;
                    logger.info("Code verified for email: {}", requestedEmail);
                    return ResponseEntity.ok("Verified for password reset");
                } else {
                    logger.warn("Incorrect code provided");
                    return ResponseEntity.badRequest().body("Incorrect code");
                }
            } else {
                logger.warn("Unauthorized access or session expired");
                return ResponseEntity.badRequest().body("You are not authorized for this section, please return to other pages :(");
            }
        } catch (Exception e) {
            logger.error("Form field is not supported", e);
            return ResponseEntity.badRequest().body("Form field is not supported, please contact us for further support");
        }
    }

    @PutMapping("/password")
    public ResponseEntity<String> checkPassword(@RequestParam("newPassword") String np, @RequestParam("renewPassword") String rnp, HttpSession session) {
        try {
            logger.info("Password reset request for email: {}, verified: {}", requestedEmail, verified);
            if (requestedEmail != null && verified != null && verified) {
                try {
                    if (np.length() < 6 || rnp.length() < 6) {
                        logger.warn("Password length issue");
                        return ResponseEntity.badRequest().body("Password must contain at least 6 letters");
                    }
                    if (!np.equals(rnp)) {
                        logger.warn("Passwords don't match");
                        return ResponseEntity.badRequest().body("Passwords don't match each other");
                    }
                    userService.findUserByPassword(np);
                    logger.warn("Old password used");
                    return ResponseEntity.badRequest().body("You may have used an old password, please try a new one.");
                } catch (Exception e2) {
                    userService.passwordChanger(requestedEmail, np);
                    session.invalidate();
                    logger.info("Password reset successful for email: {}", requestedEmail);
                    requestedEmail = null;
                    requestedCode = null;
                    verified = false;
                    return ResponseEntity.ok().body("Successfully reset password");
                }
            } else {
                logger.warn("Unauthorized access or session expired");
                return ResponseEntity.badRequest().body("You are not authorized for this section, please return to other pages :(");
            }
        } catch (Exception e) {
            logger.error("Form field is invalid", e);
            return ResponseEntity.badRequest().body("Form field is invalid, please contact us for further support");
        }
    }

    private void sendSimpleMessage(String receiverEmail, String requestedCode) {
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
                "Pixel Pioneer Course Administration Team.");
        mailSender.send(message);
    }
}

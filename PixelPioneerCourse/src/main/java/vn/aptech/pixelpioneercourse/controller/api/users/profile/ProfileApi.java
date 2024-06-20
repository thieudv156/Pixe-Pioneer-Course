package vn.aptech.pixelpioneercourse.controller.api.users.profile;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import vn.aptech.pixelpioneercourse.dto.UserCreateDto;
import vn.aptech.pixelpioneercourse.dto.UserDto;
import vn.aptech.pixelpioneercourse.dto.UserInformation;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.service.UserService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/profile")
public class ProfileApi {

    @Autowired
    private UserService service;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JavaMailSender mailSender;

    private String requestedCode = null;

    private UserCreateDto userUpdated = null;

    private void sendSimpleMessage(String receiverEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        requestedCode = service.codeGeneratorForEmailVerification();
        message.setFrom("bot@PIXELPIONEERCOURSE.com");
        message.setTo(receiverEmail);
        message.setSubject("Email Verification - Code Verifying Letter from PIXEL PIONEER COURSE");
        message.setText("Dear " + receiverEmail + ",\n\n" +
                "Thank you for being our member. To complete your email changing process, please enter the following verification code:\n\n" +
                "**" + requestedCode + "**\n\n" +
                "This code will be unavailable right after you correctly enter it." + "\n" +
                "If you did not request this code, please ignore this email or contact support.\n\n" +
                "Best regards,\n" +
                "Pixel Pioneer Course Administration Team.");
        mailSender.send(message);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserByID(@PathVariable("id") Integer id) {
        UserDto result = service.findByID(id);
        result.setPassword(""); // enhance security
        return ResponseEntity.ok(result);
    }

    @GetMapping("/instructor")
    public ResponseEntity<List<UserInformation>> getAllInstructors() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllInstructors());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/changeInformation/{id}")
    public ResponseEntity<String> changeInformation(@RequestBody UserCreateDto dto, @PathVariable("id") int uID, HttpSession session) {
        try {
            User pre_u = service.findById(uID);
            if (!pre_u.getEmail().equals(dto.getEmail())) {
                sendSimpleMessage(dto.getEmail());
                userUpdated = dto;
                return ResponseEntity.status(HttpStatus.OK).body("Code has been sent to your new email, please check.");
            }
            service.updateWithRole(mapper.map(dto, User.class), uID);
            return ResponseEntity.status(HttpStatus.OK).body("Update Profile Successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/changeInformation/{id}/{code}")
    public ResponseEntity<String> changeInformationWithinVerificationCode(@PathVariable("id") int uid, @PathVariable("code") String code, HttpSession session) {
        try {
            if (requestedCode.equals(code)) {
                service.updateWithRole(mapper.map(userUpdated, User.class), uid);
                session.removeAttribute("updatedUser");
                requestedCode = null;
                return ResponseEntity.status(HttpStatus.OK).body("Update Profile Successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid code, please try again.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}



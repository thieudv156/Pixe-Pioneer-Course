package vn.aptech.pixelpioneercourse.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.User;
@Service
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendDeletionEmail(Enrollment enrollment, String reason) {
        User user = enrollment.getUser();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("dvk1231@gmail.com");
        message.setTo(user.getEmail());
        message.setSubject("Enrollment Deletion Notification");
        message.setText("Dear " + user.getFullName() + ",\n\n" +
                "Your enrollment for the subscription type: " + enrollment.getSubscriptionType() + " which was due to end on: " + enrollment.getFormattedSubscriptionEndDate() + " has been deleted for the following reason: " + reason + "\n\n" +
                "If you have any questions, please contact our support team.\n\n" +
                "Best regards,\n" +
                "Pixel Pioneer Course Team");
        mailSender.send(message);
    }
}

package vn.aptech.pixelpioneercourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.entities.SubscriptionType;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.EnrollmentRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public Enrollment enrollUser(Integer userId, SubscriptionType subscriptionType, PaymentMethod paymentMethod) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        LocalDateTime subscriptionEndDate;
        switch (subscriptionType) {
            case MONTHLY:
                subscriptionEndDate = LocalDateTime.now().plusMonths(1);
                break;
            case YEARLY:
                subscriptionEndDate = LocalDateTime.now().plusYears(1);
                break;
            case UNLIMITED:
                subscriptionEndDate = null; // Unlimited access
                break;
            default:
                throw new IllegalArgumentException("Invalid subscription type");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setPaymentDate(LocalDateTime.now());
        enrollment.setPaymentMethod(paymentMethod);
        enrollment.setPaymentStatus(true);
        enrollment.setSubscriptionStatus(true);
        enrollment.setSubscriptionType(subscriptionType);
        enrollment.setSubscriptionEndDate(subscriptionEndDate);
        return enrollmentRepository.save(enrollment);
    }
}

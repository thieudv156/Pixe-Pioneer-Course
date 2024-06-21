package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.dto.EnrollmentCreateDto;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.entities.SubscriptionType;

import java.util.List;

public interface EnrollmentService {
    Enrollment enrollUser(Integer userId, SubscriptionType subscriptionType, PaymentMethod paymentMethod);

    boolean isUserEnrolledAndPaid(Integer userId);
    List<Enrollment> findAll();
    Enrollment findById(Integer id);
    Enrollment save(Enrollment enrollment);
    Enrollment findByUserId(Integer id);
    List<Enrollment> findEnrollmentsByUserID(Integer id);
    void deleteById(Integer id);
    
}

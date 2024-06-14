package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.entities.SubscriptionType;

public interface EnrollmentService {
    Enrollment enrollUser(Integer userId, SubscriptionType subscriptionType, PaymentMethod paymentMethod);

    boolean isUserEnrolledAndPaid(Integer userId);
}

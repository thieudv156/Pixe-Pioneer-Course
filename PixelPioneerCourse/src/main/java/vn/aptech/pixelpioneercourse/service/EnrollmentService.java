package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.entities.SubscriptionType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
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
    List<Enrollment> get10LatestEnrollments();
    List<Enrollment> findAllByUserId(Integer id);
    ByteArrayInputStream exportEnrollmentsToExcel(List<Enrollment> enrollments) throws IOException;

    List<Enrollment> findByDateRange(LocalDate start, LocalDate end);
}

package vn.aptech.pixelpioneercourse.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.aptech.pixelpioneercourse.entities.Enrollment;
import vn.aptech.pixelpioneercourse.entities.PaymentMethod;
import vn.aptech.pixelpioneercourse.entities.SubscriptionType;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.EnrollmentRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PaymentService paymentService;

    @Transactional
    @Override
    public Enrollment enrollUser(Integer userId, SubscriptionType subscriptionType, PaymentMethod paymentMethod) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));

        double amount;
        LocalDateTime subscriptionEndDate;
        switch (subscriptionType) {
            case MONTHLY:
                amount = 9.0;
                subscriptionEndDate = LocalDateTime.now().plusMonths(1);
                break;
            case YEARLY:
                amount = 99.0;
                subscriptionEndDate = LocalDateTime.now().plusYears(1);
                break;
            case UNLIMITED:
                amount = 999.0;
                subscriptionEndDate = null; // Unlimited access
                break;
            default:
                throw new IllegalArgumentException("Invalid subscription type");
        }

        boolean paymentSuccessful = false;
        if (paymentMethod == PaymentMethod.PAYPAL) {
            paymentSuccessful = true; // PayPal payment is already processed in the controller
        } else if (paymentMethod == PaymentMethod.CREDIT_CARD) {
            paymentSuccessful = paymentService.processCreditCardPayment("mockCardNumber", "12/2025", "123", amount, "USD"); // Mock card details
        }

        if (!paymentSuccessful) {
            throw new RuntimeException("Payment failed");
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

    @Override
    public boolean isUserEnrolledAndPaid(Integer userId) {
        return enrollmentRepository.existsByUserIdAndPaymentStatusTrue(userId);
    }

    public List<Enrollment> findAll() {
        return enrollmentRepository.findAll();
    }

    public Enrollment findById(Integer id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }
    
    public Enrollment findByUserId(Integer id) {
    	return enrollmentRepository.findByUserId(id).
    			orElseThrow(() -> new RuntimeException("Enrollment not found"));
    }
    
    public List<Enrollment> findEnrollmentsByUserID(Integer id) {
    	return enrollmentRepository.findEnrollmentsByUserId(id);
    }

    public Enrollment save(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public void deleteById(Integer id) {
        enrollmentRepository.deleteById(id);
    }

    public List<Enrollment> get10LatestEnrollments() {
        return enrollmentRepository.findTop10ByOrderByEnrolledAtDesc();
    }
    
    public List<Enrollment> findAllByUserId(Integer id) {
    	return enrollmentRepository.findAllByUserId(id, Sort.by(Sort.Direction.DESC, "enrolledAt"));
    }

    public ByteArrayInputStream exportEnrollmentsToExcel(List<Enrollment> enrollments) throws IOException {
        String[] COLUMNs = {"ID", "Name", "Payment Date","Payment Method", "Subscription End Date", "Status"};
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Enrollments");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < COLUMNs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(COLUMNs[col]);
            }

            int rowIdx = 1;
            for (Enrollment enrollment : enrollments) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(enrollment.getId());
                row.createCell(1).setCellValue(enrollment.getUser().getFullName());
                row.createCell(2).setCellValue(enrollment.getPaymentDate().toLocalDate().format(formatter));
                row.createCell(3).setCellValue(enrollment.getPaymentMethod().toString());
                row.createCell(4).setCellValue(enrollment.getSubscriptionEndDate().toLocalDate().format(formatter));
                row.createCell(5).setCellValue(enrollment.isSubcriptionActive() ? "Active" : "Inactive");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    @Override
    public List<Enrollment> findByDateRange(LocalDate start, LocalDate end) {
        return enrollmentRepository.findAllByPaymentDateBetween(start.atStartOfDay(), end.atTime(23, 59, 59));
    }
}

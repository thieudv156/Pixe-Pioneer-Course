package vn.aptech.pixelpioneercourse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.aptech.pixelpioneercourse.entities.User;
import vn.aptech.pixelpioneercourse.repository.EnrollmentRepository;
import vn.aptech.pixelpioneercourse.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    private static final double MONTHLY_PRICE = 9.0;
    private static final double YEARLY_PRICE = 99.0;
    private static final double UNLIMITED_PRICE = 999.0;

    @Override
    public double getEarningsThisMonth() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

        Integer monthlyEnrollments = enrollmentRepository.countMonthlyEnrollments(startOfMonth, endOfMonth);
        Integer yearlyEnrollments = enrollmentRepository.countYearlyEnrollments(startOfMonth, endOfMonth);
        Integer unlimitedEnrollments = enrollmentRepository.countUnlimitedEnrollments(startOfMonth, endOfMonth);

        return (monthlyEnrollments * MONTHLY_PRICE) + (yearlyEnrollments * YEARLY_PRICE) + (unlimitedEnrollments * UNLIMITED_PRICE);
    }

    @Override
    public List<User> getTotalActiveUsers() {
        return userRepository.findAllByActiveStatusIsTrue();
    }

    @Override
    public Integer getMonthlyEnrollmentsTotal() {
        return enrollmentRepository.countAllBySubscriptionTypeMonthly();
    }

    @Override
    public Integer getYearlyEnrollmentsTotal() {
        return enrollmentRepository.countAllBySubscriptionTypeYearly();
    }

    @Override
    public Integer getUnlimitedEnrollmentsTotal() {
        return enrollmentRepository.countAllBySubscriptionTypeUnlimited();
    }

    @Override
    public double getTotalSales() {
        Integer monthlyEnrollments = getMonthlyEnrollmentsTotal();
        Integer yearlyEnrollments = getYearlyEnrollmentsTotal();
        Integer unlimitedEnrollments = getUnlimitedEnrollmentsTotal();

        return (monthlyEnrollments * MONTHLY_PRICE) + (yearlyEnrollments * YEARLY_PRICE) + (unlimitedEnrollments * UNLIMITED_PRICE);
    }

    @Override
    public Map<LocalDate, Double> getDailySales(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<Object[]> results = enrollmentRepository.findDailySalesBetweenDates(startDateTime, endDateTime);
        Map<LocalDate, Double> salesMap = new HashMap<>();
        for (Object[] result : results) {
            LocalDateTime dateTime = (LocalDateTime) result[0];
            LocalDate date = dateTime.toLocalDate();
            long countMonthly = ((Number) result[1]).longValue();
            long countYearly = ((Number) result[2]).longValue();
            long countUnlimited = ((Number) result[3]).longValue();
            Double dailySales = (countMonthly * MONTHLY_PRICE) + (countYearly * YEARLY_PRICE) + (countUnlimited * UNLIMITED_PRICE);
            salesMap.put(date, dailySales);
        }

        // Fill the map with 0 sales for days without enrollments
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            salesMap.putIfAbsent(currentDate, 0.0);
            currentDate = currentDate.plusDays(1);
        }

        return salesMap;
    }

    @Override
    public Map<LocalDate, Double> getDailySalesThisWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = now.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        return getDailySales(startOfWeek, endOfWeek);
    }

    @Override
    public Map<LocalDate, Double> getDailySalesLastWeek() {
        LocalDate now = LocalDate.now();
        LocalDate startOfLastWeek = now.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).minusWeeks(1);
        LocalDate endOfLastWeek = startOfLastWeek.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY));
        return getDailySales(startOfLastWeek, endOfLastWeek);
    }
}




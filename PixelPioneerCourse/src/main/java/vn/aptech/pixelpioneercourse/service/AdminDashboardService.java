package vn.aptech.pixelpioneercourse.service;

import vn.aptech.pixelpioneercourse.entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AdminDashboardService {
    double getEarningsThisMonth();

    List<User> getTotalActiveUsers();

    Integer getMonthlyEnrollmentsTotal();

    Integer getYearlyEnrollmentsTotal();

    Integer getUnlimitedEnrollmentsTotal();

    double getTotalSales();

    Map<LocalDate, Double> getDailySales(LocalDate startDate, LocalDate endDate);

    Map<LocalDate, Double> getDailySalesThisWeek();

    Map<LocalDate, Double> getDailySalesLastWeek();
}

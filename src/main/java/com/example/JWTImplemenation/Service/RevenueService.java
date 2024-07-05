package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.Repository.OrderRepository;
import com.example.JWTImplemenation.Service.IService.IRevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class RevenueService implements IRevenueService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Map<String, Object> calculateRevenueByMonth(int year) {
        Map<String, Object> revenueData = new HashMap<>();
        for (int month = 1; month <= 12; month++) {
            Timestamp start = Timestamp.valueOf(LocalDate.of(year, month, 1).atStartOfDay());
            Timestamp end = Timestamp.valueOf(LocalDate.of(year, month, start.toLocalDateTime().toLocalDate().lengthOfMonth()).atTime(23, 59, 59));
            Integer revenue = orderRepository.calculateRevenueBetweenDates(start, end);
            revenueData.put("Month " + month, revenue == null ? 0 : revenue);
        }
        return revenueData;
    }

    @Override
    public Map<String, Object> calculateRevenueByYear() {
        Map<String, Object> revenueData = new HashMap<>();
        int currentYear = LocalDate.now().getYear();
        for (int year = 2020; year <= currentYear; year++) { // Adjust the starting year as needed
            Timestamp start = Timestamp.valueOf(LocalDate.of(year, 1, 1).atStartOfDay());
            Timestamp end = Timestamp.valueOf(LocalDate.of(year, 12, 31).atTime(23, 59, 59));
            Integer revenue = orderRepository.calculateRevenueBetweenDates(start, end);
            revenueData.put("Year " + year, revenue == null ? 0 : revenue);
        }
        return revenueData;
    }

    @Override
    public Map<String, Object> calculateRevenueByDateRange(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Timestamp start = Timestamp.valueOf(LocalDate.parse(startDate, formatter).atStartOfDay());
        Timestamp end = Timestamp.valueOf(LocalDate.parse(endDate, formatter).atTime(23, 59, 59));
        Integer revenue = orderRepository.calculateRevenueBetweenDates(start, end);
        Map<String, Object> revenueData = new HashMap<>();
        revenueData.put("Start Date", startDate);
        revenueData.put("End Date", endDate);
        revenueData.put("Revenue", revenue == null ? 0 : revenue);
        return revenueData;
    }
}

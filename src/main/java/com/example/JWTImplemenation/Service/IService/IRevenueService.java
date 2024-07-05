package com.example.JWTImplemenation.Service.IService;

import java.util.Map;

public interface IRevenueService {
    Map<String, Object> calculateRevenueByMonth(int year);
    Map<String, Object> calculateRevenueByYear();
    Map<String, Object> calculateRevenueByDateRange(String startDate, String endDate);
}

package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUserId(Integer userId);
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.createdDate BETWEEN :startDate AND :endDate")
    Integer calculateRevenueBetweenDates(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
}

package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    // Define any specific query methods if needed
}
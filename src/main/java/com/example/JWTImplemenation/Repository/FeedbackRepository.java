package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback> findAllByProduct(Product product);


    Optional<Feedback> findByUserAndOrderItem(User user, OrderItem orderItem);


    Optional<Feedback> findByOrderItemId(Integer orderItemId);
}

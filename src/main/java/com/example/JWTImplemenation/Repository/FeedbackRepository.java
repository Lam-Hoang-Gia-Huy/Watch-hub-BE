package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.Feedback;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Entities.Watch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findByUser(User user);

    boolean existsByBuyerAndWatchAndUser(User buyer, Watch watch, User user);
}

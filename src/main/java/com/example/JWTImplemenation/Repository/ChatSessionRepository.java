package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {

    List<ChatSession> findBySellerIdOrAppraiserId(Integer userId, Integer userId1);

    List<ChatSession> findBySellerIdAndAppraiserIdAndWatchId(Integer userId, Integer appraiserId, Integer watchId);
}
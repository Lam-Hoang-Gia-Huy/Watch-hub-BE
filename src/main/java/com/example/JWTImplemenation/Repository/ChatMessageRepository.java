package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatSessionId(Integer chatSessionId);

}

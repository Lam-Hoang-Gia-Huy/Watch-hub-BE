package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.ChatMessageDTO;
import com.example.JWTImplemenation.DTO.ChatSessionDTO;
import com.example.JWTImplemenation.DTO.ChatStartRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IChatService {
    ResponseEntity<ChatSessionDTO> startChatSession(ChatStartRequest chatStartRequest);
    ResponseEntity<List<ChatSessionDTO>> getChatSessionsByUserId(Integer userId);
    ResponseEntity<List<ChatMessageDTO>> getChatMessages(Integer sessionId);
    ResponseEntity<ChatMessageDTO> sendChatMessage(Integer sessionId, String content, Integer senderId);
}

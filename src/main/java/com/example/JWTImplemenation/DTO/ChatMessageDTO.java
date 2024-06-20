package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Integer id;
    private Integer chatSessionId;
    private Integer senderId;
    private String message;
    private Timestamp timestamp;
}

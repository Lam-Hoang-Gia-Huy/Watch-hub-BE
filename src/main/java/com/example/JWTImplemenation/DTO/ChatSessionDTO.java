package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSessionDTO {
    private Integer id;
    private ProductDTO product;
    private UserDTO staff;
    private UserDTO user;
    private Timestamp createdDate;
    private List<ChatMessageDTO> messages;
}


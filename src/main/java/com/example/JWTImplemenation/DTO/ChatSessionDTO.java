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
    private WatchDTO watch;
    private UserDTO appraiser;
    private UserDTO seller;
    private Timestamp createdDate;
    private List<ChatMessageDTO> messages;
}

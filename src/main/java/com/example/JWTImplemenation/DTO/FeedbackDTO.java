package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
    private Integer id;
    private Integer userId;
    private Integer productId;
    private Integer orderItemId;
    private String comment;
    private int score;
    private Timestamp createdDate;
}

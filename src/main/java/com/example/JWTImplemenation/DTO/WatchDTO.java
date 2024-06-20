package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchDTO {
    private Integer id;
    private String name;
    private String brand;
    private String description;
    private boolean isPaid;
    private boolean status;
    private Integer price;
    private Timestamp createdDate;
    private List<String> imageUrl;
    private Integer appraisalId;
    private Integer sellerId;
}
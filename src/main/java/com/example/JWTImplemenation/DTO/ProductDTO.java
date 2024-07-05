package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private String category;
    private String description;
    private int stockQuantity;
    private boolean status;
    private Integer price;
    private Timestamp createdDate;
    private List<String> imageUrl;
}
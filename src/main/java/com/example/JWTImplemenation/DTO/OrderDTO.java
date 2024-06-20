package com.example.JWTImplemenation.DTO;

import com.example.JWTImplemenation.Entities.Watch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Integer id;
    private Integer userId;
    private List<OrderItemDTO> orderItems;
    private Integer totalAmount;
    private Timestamp createdDate;
    private Timestamp lastModifiedDate;
}

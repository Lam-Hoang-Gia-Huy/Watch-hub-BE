package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Integer id;
    private ProductDTO product;
    private Integer quantity;
}

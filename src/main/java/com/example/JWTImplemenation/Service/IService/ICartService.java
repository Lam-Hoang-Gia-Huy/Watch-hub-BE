package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.CartDTO;
import com.example.JWTImplemenation.DTO.CartItemDTO;
import com.example.JWTImplemenation.Entities.CartItem;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICartService {
    ResponseEntity<CartDTO> findCartByUserId(Integer userId);
    ResponseEntity<CartItemDTO> addToCart(Integer userId, CartItemDTO cartItemRequest);
    ResponseEntity<Void> removeFromCart(Integer userId, Integer cartItemId);
    void clearCart(Integer userId);
    List<Integer> findWatchIdsInCart(Integer userId);
}

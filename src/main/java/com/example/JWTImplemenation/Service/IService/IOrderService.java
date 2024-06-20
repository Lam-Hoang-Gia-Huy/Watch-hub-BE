package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.OrderDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IOrderService {
    ResponseEntity<OrderDTO> createOrder(OrderDTO orderDTO);
    ResponseEntity<List<OrderDTO>> getAllOrders();
    ResponseEntity<List<OrderDTO>> getOrdersByUserId(Integer userId);
    ResponseEntity<OrderDTO> getOrderById(Integer orderId);
}

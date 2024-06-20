package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.OrderDTO;
import com.example.JWTImplemenation.DTO.OrderItemDTO;
import com.example.JWTImplemenation.DTO.WatchDTO;
import com.example.JWTImplemenation.Entities.*;
import com.example.JWTImplemenation.Repository.OrderRepository;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Repository.WatchRespository;
import com.example.JWTImplemenation.Service.IService.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchRespository watchRepository;

    @Override
    public ResponseEntity<OrderDTO> createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);
        order.setTotalAmount(orderDTO.getTotalAmount());

        List<OrderItem> orderItems = orderDTO.getOrderItems().stream().map(itemDTO -> {
            OrderItem orderItem = new OrderItem();
            Watch watch = watchRepository.findById(itemDTO.getWatch().getId())
                    .orElseThrow(() -> new RuntimeException("Watch not found"));
            orderItem.setWatch(watch);
            orderItem.setPrice(itemDTO.getWatch().getPrice());
            orderItem.setOrder(order);
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(convertToDTO(savedOrder));
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @Override
    public ResponseEntity<List<OrderDTO>> getOrdersByUserId(Integer userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDTO> orderDTOs = orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDTOs);
    }

    @Override
    public ResponseEntity<OrderDTO> getOrderById(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setUserId(order.getUser().getId());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setCreatedDate(order.getCreatedDate());
        orderDTO.setLastModifiedDate(order.getLastModifiedDate());

        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::convertOrderItemToDTO)
                .collect(Collectors.toList());
        orderDTO.setOrderItems(orderItemDTOs);

        return orderDTO;
    }

    private OrderItemDTO convertOrderItemToDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        WatchDTO watchDTO = convertWatchToDTO(orderItem.getWatch());
        orderItemDTO.setWatch(watchDTO);
        return orderItemDTO;
    }

    private WatchDTO convertWatchToDTO(Watch watch) {
        WatchDTO watchDTO = new WatchDTO();
        watchDTO.setId(watch.getId());
        watchDTO.setName(watch.getName());
        watchDTO.setBrand(watch.getBrand());
        watchDTO.setDescription(watch.getDescription());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setPaid(watch.isPaid());
        watchDTO.setStatus(watch.isStatus());
        watchDTO.setCreatedDate(watch.getCreatedDate());
        if (watch.getImageUrl() != null) {
            List<String> imageUrls = watch.getImageUrl()
                    .stream()
                    .map(ImageUrl::getImageUrl)
                    .collect(Collectors.toList());
            watchDTO.setImageUrl(imageUrls);
        }
        if (watch.getAppraisal() != null) {
            watchDTO.setAppraisalId(watch.getAppraisal().getId());
        }
        watchDTO.setSellerId(watch.getUser().getId());
        return watchDTO;
    }
}

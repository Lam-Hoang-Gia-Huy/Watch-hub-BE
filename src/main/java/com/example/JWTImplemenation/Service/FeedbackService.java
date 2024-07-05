package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.FeedbackDTO;
import com.example.JWTImplemenation.Entities.Feedback;
import com.example.JWTImplemenation.Entities.Order;
import com.example.JWTImplemenation.Entities.OrderItem;
import com.example.JWTImplemenation.Entities.Product;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Repository.*;
import com.example.JWTImplemenation.Service.IService.IFeedbackservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedbackService implements IFeedbackservice {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public ResponseEntity<FeedbackDTO> createFeedback(FeedbackDTO feedbackDTO) {
        User user = userRepository.findById(feedbackDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Product product = productRepository.findById(feedbackDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        OrderItem orderItem = orderItemRepository.findById(feedbackDTO.getOrderItemId())
                .orElseThrow(() -> new RuntimeException("Order item not found"));

        Feedback existingFeedback = feedbackRepository.findByUserAndOrderItem(user,  orderItem)
                .orElse(null);
        if (existingFeedback != null) {
            throw new RuntimeException("Feedback already exists for this order item");
        }

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setProduct(product);
        feedback.setOrderItem(orderItem);
        feedback.setComment(feedbackDTO.getComment());
        feedback.setScore(feedbackDTO.getScore());

        Feedback savedFeedback = feedbackRepository.save(feedback);
        updateProductAverageScore(product);
        return ResponseEntity.ok(convertToDTO(savedFeedback));
    }

    @Override
    public ResponseEntity<FeedbackDTO> updateFeedback(Integer feedbackId, FeedbackDTO feedbackDTO) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        // Check if the feedback belongs to the logged-in user
        if (!feedback.getUser().getId().equals(feedbackDTO.getUserId())) {
            throw new RuntimeException("You are not authorized to update this feedback");
        }

        feedback.setComment(feedbackDTO.getComment());
        feedback.setScore(feedbackDTO.getScore());
        Feedback updatedFeedback = feedbackRepository.save(feedback);
        updateProductAverageScore(feedback.getProduct());
        return ResponseEntity.ok(convertToDTO(updatedFeedback));
    }

    @Override
    public ResponseEntity<FeedbackDTO> getFeedbackByProductAndOrder(Integer orderItemId) {
        Feedback feedback = feedbackRepository.findByOrderItemId(orderItemId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        return ResponseEntity.ok(convertToDTO(feedback));
    }
    private void updateProductAverageScore(Product product) {
        List<Feedback> feedbacks = feedbackRepository.findAllByProduct(product);
        double averageScore = feedbacks.stream()
                .mapToInt(Feedback::getScore)
                .average()
                .orElse(0.0);
        product.setAverageScore(averageScore);
        productRepository.save(product);
    }

    private FeedbackDTO convertToDTO(Feedback feedback) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setId(feedback.getId());
        feedbackDTO.setUserId(feedback.getUser().getId());
        feedbackDTO.setProductId(feedback.getProduct().getId());
        feedbackDTO.setOrderItemId(feedback.getOrderItem().getId()); // Ensure orderItemId is included in DTO
        feedbackDTO.setComment(feedback.getComment());
        feedbackDTO.setScore(feedback.getScore());
        feedbackDTO.setCreatedDate(feedback.getCreatedDate());
        return feedbackDTO;
    }
}

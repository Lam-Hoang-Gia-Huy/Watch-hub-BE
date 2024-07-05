package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.FeedbackDTO;
import com.example.JWTImplemenation.Entities.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IFeedbackservice {
    ResponseEntity<FeedbackDTO> createFeedback(FeedbackDTO feedbackDTO);
    ResponseEntity<FeedbackDTO> updateFeedback(Integer feedbackId, FeedbackDTO feedbackDTO);
    ResponseEntity<FeedbackDTO> getFeedbackByProductAndOrder(Integer orderItemId);
}

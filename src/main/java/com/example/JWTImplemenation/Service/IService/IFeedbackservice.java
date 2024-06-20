package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.FeedbackDTO;
import org.springframework.http.ResponseEntity;

public interface IFeedbackservice {
    ResponseEntity<FeedbackDTO> addFeedback(FeedbackDTO feedbackDTO);

}

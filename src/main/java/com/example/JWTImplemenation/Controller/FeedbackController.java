package com.example.JWTImplemenation.Controller;

import com.example.JWTImplemenation.DTO.FeedbackDTO;
import com.example.JWTImplemenation.Service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackDTO> addFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        return feedbackService.addFeedback(feedbackDTO);
    }
}

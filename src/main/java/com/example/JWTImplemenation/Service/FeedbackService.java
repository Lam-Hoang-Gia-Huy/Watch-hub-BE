package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.FeedbackDTO;
import com.example.JWTImplemenation.Entities.Feedback;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Repository.FeedbackRepository;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Repository.WatchRespository;
import com.example.JWTImplemenation.Service.IService.IFeedbackservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService implements IFeedbackservice {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WatchRespository watchRepository;

    @Override
    public ResponseEntity<FeedbackDTO> addFeedback(FeedbackDTO feedbackDTO) {
        Optional<User> userOptional = userRepository.findById(feedbackDTO.getUserId());
        Optional<User> buyerOptional = userRepository.findById(feedbackDTO.getBuyerId());
        Optional<Watch> watchOptional = watchRepository.findById(feedbackDTO.getWatchId());
        if (userOptional.isPresent() && watchOptional.isPresent()&& buyerOptional.isPresent()) {
            User user = userOptional.get();
            Watch watch = watchOptional.get();
            User buyer = buyerOptional.get();
            boolean feedbackExists = feedbackRepository.existsByBuyerAndWatchAndUser(buyer, watch,user);
            if (feedbackExists) {
                return ResponseEntity.badRequest().body(null);
            }

            Feedback feedback = Feedback.builder()
                    .buyer(buyer)
                    .user(user)
                    .watch(watch)
                    .comments(feedbackDTO.getComments())
                    .rating(feedbackDTO.getRating())
                    .createdDate(new Timestamp(System.currentTimeMillis()))
                    .build();

            Feedback savedFeedback = feedbackRepository.save(feedback);
            updateUserRatings(user);

            return ResponseEntity.ok(convertToDTO(savedFeedback));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private void updateUserRatings(User user) {
        List<Feedback> feedbacks = feedbackRepository.findByUser(user);
        double averageRating = feedbacks.stream().mapToInt(Feedback::getRating).average().orElse(0.0);
        user.setRating(averageRating);
        userRepository.save(user);
    }

    private FeedbackDTO convertToDTO(Feedback feedback) {
        return FeedbackDTO.builder()
                .id(feedback.getId())
                .buyerId(feedback.getBuyer().getId())
                .userId(feedback.getUser().getId())
                .watchId(feedback.getWatch().getId())
                .comments(feedback.getComments())
                .rating(feedback.getRating())
                .createdDate(feedback.getCreatedDate())
                .build();
    }
}

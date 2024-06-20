package com.example.JWTImplemenation.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_session")
public class ChatSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "watch_id", nullable = false)
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "appraiser_id", nullable = false)
    private User appraiser;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @CreatedDate
    private Timestamp createdDate;

    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ChatMessage> messages= new ArrayList<>();
}

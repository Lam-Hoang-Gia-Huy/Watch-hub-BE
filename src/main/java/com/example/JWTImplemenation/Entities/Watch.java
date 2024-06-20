package com.example.JWTImplemenation.Entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table
public class Watch {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String brand;
    @CreatedDate
    private Timestamp createdDate;
    private String description;
    private Integer price;
    private boolean status;
    private boolean isPaid;

    @OneToOne(mappedBy = "watch", cascade = CascadeType.ALL)
    private Appraisal appraisal;

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL)
    private List<ImageUrl> imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;
}


package com.example.JWTImplemenation.Entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Appraisal {
    @Id
    @GeneratedValue
    private Integer id;
    private String comments;
    private Integer value;
    private String material;
    private String thickness;
    private String dial;
    private String movement;
    private String crystal;
    private String bracket;
    private String buckle;

    @OneToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

    @ManyToOne
    @JoinColumn(name = "appraiser_id")
    private User appraiser;
}


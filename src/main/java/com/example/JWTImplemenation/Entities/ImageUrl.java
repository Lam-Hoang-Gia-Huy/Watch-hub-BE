package com.example.JWTImplemenation.Entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.JWTImplemenation.Entities.Watch;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ImageUrl {
    @Id
    @GeneratedValue
    private Integer id;
    private String imageUrl;
    @ManyToOne
    private Watch watch;
}

package com.example.JWTImplemenation.Repository;
import com.example.JWTImplemenation.Entities.Watch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchRespository extends JpaRepository<Watch, Integer> {
    List<Watch> findByUserId(Integer userId);
}

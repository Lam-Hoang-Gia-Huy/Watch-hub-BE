package com.example.JWTImplemenation.Repository;
import com.example.JWTImplemenation.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT w FROM Product w WHERE " +
            "(:name IS NULL OR LOWER(w.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:category IS NULL OR LOWER(w.category) LIKE LOWER(CONCAT('%', :category, '%'))) AND " +
            "(:minPrice IS NULL OR w.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR w.price <= :maxPrice)")
    List<Product> searchWatches(String name, String category, Integer minPrice, Integer maxPrice);
}

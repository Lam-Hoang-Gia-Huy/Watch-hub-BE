package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.ProductDTO;
import com.example.JWTImplemenation.Entities.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    ResponseEntity<List<ProductDTO>> findAll();
     ResponseEntity<ProductDTO> findById(Integer id);
    ResponseEntity<ProductDTO> save(Product product);
    ResponseEntity<ProductDTO> update(Integer id, Product product);
    ResponseEntity<Void> deleteById(Integer id);
    ResponseEntity<List<ProductDTO>> searchProducts(String name, String brand, Integer minPrice, Integer maxPrice);
    ResponseEntity<ProductDTO> addImagesToWatch(Integer watchId, List<MultipartFile> imageFiles);

    ResponseEntity<ProductDTO> update(Integer id, ProductDTO productDTO);

    void updateWatchStatus(List<Integer> watchIds, boolean status, boolean isPaid);

}

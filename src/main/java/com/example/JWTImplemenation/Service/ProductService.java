package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.ProductDTO;
import com.example.JWTImplemenation.Entities.ImageUrl;
import com.example.JWTImplemenation.Entities.Product;
import com.example.JWTImplemenation.Repository.ProductRepository;
import com.example.JWTImplemenation.Service.IService.IImageService;
import com.example.JWTImplemenation.Service.IService.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final IImageService imageService;

    @Override
    public ResponseEntity<List<ProductDTO>> findAll() {
        List<Product> products = productRepository.findAll();
        return ResponseEntity.ok(convertToDTOList(products));
    }

    @Override
    public ResponseEntity<ProductDTO> findById(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ProductDTO> save(Product product) {
        product.setStatus(true);
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(convertToDTO(savedProduct));
    }

    @Override
    public ResponseEntity<ProductDTO> update(Integer id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteById(Integer id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStatus(false); // Set status to false (0)
            productRepository.save(product);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<ProductDTO>> searchProducts(String name, String category, Integer minPrice, Integer maxPrice) {
        List<Product> products = productRepository.searchWatches(name, category, minPrice, maxPrice);
        return ResponseEntity.ok(convertToDTOList(products));
    }

    @Override
    public ResponseEntity<ProductDTO> addImagesToWatch(Integer productId, List<MultipartFile> imageFiles) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            List<ImageUrl> imageUrls = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                String imageUrl = imageService.uploadImage(imageFile);
                if (imageUrl != null) {
                    ImageUrl image = new ImageUrl();
                    image.setImageUrl(imageUrl);
                    image.setProduct(product);
                    imageUrls.add(image);
                }
            }
            if (product.getImageUrl() == null) {
                product.setImageUrl(new ArrayList<>());
            }
            product.getImageUrl().addAll(imageUrls);
            Product updatedProduct = productRepository.save(product);
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        }
        return ResponseEntity.notFound().build();
    }
    @Override
    public ResponseEntity<ProductDTO> update(Integer id, ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(productDTO.getCategory());
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
            existingProduct.setStatus(productDTO.isStatus());


            Product updatedProduct = productRepository.save(existingProduct);
            return ResponseEntity.ok(convertToDTO(updatedProduct));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategory(product.getCategory());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStockQuantity(product.getStockQuantity());
        productDTO.setStatus(product.isStatus());
        productDTO.setAverageScore(product.getAverageScore());
        productDTO.setCreatedDate(product.getCreatedDate());
        if (product.getImageUrl() != null) {
            List<String> imageUrls = product.getImageUrl()
                    .stream()
                    .map(ImageUrl::getImageUrl)
                    .collect(Collectors.toList());
            productDTO.setImageUrl(imageUrls);
        }
        return productDTO;
    }

    private List<ProductDTO> convertToDTOList(List<Product> products) {
        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void updateWatchStatus(List<Integer> watchIds, boolean status, boolean isPaid) {
        for (Integer id : watchIds) {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                product.setStatus(status);
                productRepository.save(product);
            }
        }
    }


}

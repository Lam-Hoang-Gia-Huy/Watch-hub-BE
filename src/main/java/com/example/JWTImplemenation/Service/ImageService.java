package com.example.JWTImplemenation.Service;

import com.cloudinary.Cloudinary;
import com.example.JWTImplemenation.Service.IService.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service

public class ImageService implements IImageService {
    @Autowired
    private Cloudinary cloudinary;

    public String uploadImage(MultipartFile imageFile){
        try{
        Map<String, Object> uploadOptions = new HashMap<>();
        uploadOptions.put("resource_type", "auto"); // Automatically detect image type

        Map<String, String> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), uploadOptions);
        return uploadResult.get("secure_url"); // Get the secure image URL
    }catch (IOException io) {
            throw new RuntimeException("Image upload failed");
        }
        }
}

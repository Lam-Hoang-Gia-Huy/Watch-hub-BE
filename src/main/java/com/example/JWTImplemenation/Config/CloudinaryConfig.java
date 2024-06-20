package com.example.JWTImplemenation.Config;

import com.cloudinary.Cloudinary;
import lombok.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dfeuv0ynf");
        config.put("api_key", "841474939877546");
        config.put("api_secret", "TpLGUxHYRVRn7l5z5T6o-tn3Ivs");
        return new Cloudinary(config);
    }
}
package com.example.JWTImplemenation.DTO;

import com.example.JWTImplemenation.Entities.Enum.Gender;
import com.example.JWTImplemenation.Entities.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Integer id;
    private String password;
    private String name;
    private String email;
    private String avatarUrl;
    private Timestamp createdDate;
    private boolean status;
    @Enumerated(EnumType.STRING)
    private Role role;
}

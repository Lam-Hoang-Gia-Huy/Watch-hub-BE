package com.example.JWTImplemenation.DTO;

import com.example.JWTImplemenation.Entities.Enum.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String address;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    public Timestamp createdDate;
}

package com.example.JWTImplemenation.Response;

import com.example.JWTImplemenation.DTO.UserDTO;
import com.example.JWTImplemenation.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private UserDTO user;
    private String token;
    private String refreshToken;
}

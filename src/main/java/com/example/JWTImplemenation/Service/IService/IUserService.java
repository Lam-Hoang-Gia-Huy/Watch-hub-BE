package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.UserDTO;
import com.example.JWTImplemenation.Entities.User;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    ResponseEntity<List<UserDTO>> findAll();
    ResponseEntity<UserDTO> findById(Integer id);
    ResponseEntity<UserDTO> update(Integer id, UserDTO userDTO);
    ResponseEntity<Void> deleteById(Integer id);
}


package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.UserDTO;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Service.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(convertToDTOList(users));
    }

    public ResponseEntity<UserDTO> findById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<UserDTO> update(Integer id, UserDTO userDTO) {
        if (userRepository.existsById(id)) {
            userDTO.setId(id);
            User user = convertToEntity(userDTO);
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(convertToDTO(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteById(Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .rating(user.getRating())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .address(user.getAddress())
                .status(user.isStatus())
                .role(user.getRole())
                .createdDate(user.getCreatedDate())
                .build();
    }

    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .password(userDTO.getPassword())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .gender(userDTO.getGender())
                .avatarUrl(userDTO.getAvatarUrl())
                .address(userDTO.getAddress())
                .status(userDTO.isStatus())
                .role(userDTO.getRole())
                .createdDate(userDTO.getCreatedDate())
                .build();
    }

    private List<UserDTO> convertToDTOList(List<User> users) {
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


}

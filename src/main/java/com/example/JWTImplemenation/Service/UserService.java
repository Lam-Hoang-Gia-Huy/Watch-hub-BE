package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.UserDTO;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Service.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ImageService imageService;

    public ResponseEntity<List<UserDTO>> findAll() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(convertToDTOList(users));
    }

    public ResponseEntity<UserDTO> findById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<UserDTO> update(Integer id, UserDTO userDTO, MultipartFile avatarFile) {
        if (userRepository.existsById(id)) {
            userDTO.setId(id);
            User user = convertToEntity(userDTO);
            if (avatarFile != null && !avatarFile.isEmpty()) {
                String avatarUrl = imageService.uploadImage(avatarFile);
                user.setAvatarUrl(avatarUrl);
            }
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(convertToDTO(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> deleteById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(false);
            userRepository.save(user);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    public ResponseEntity<Void> activateUser(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setStatus(true);
            userRepository.save(user);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .password(user.getPassword())
                .name(user.getName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .status(user.isStatus())
                .role(user.getRole())
                .createdDate(user.getCreatedDate())
                .build();
    }

    private User convertToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .password(userDTO.getPassword())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .avatarUrl(userDTO.getAvatarUrl())
                .status(userDTO.isStatus())
                .role(userDTO.getRole())
                .createdDate(userDTO.getCreatedDate())
                .build();
    }

    private List<UserDTO> convertToDTOList(List<User> users) {
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }


}

package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.Role;
import com.example.JWTImplemenation.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    List<User> findByRoleAndStatus(Role role, boolean status);

}

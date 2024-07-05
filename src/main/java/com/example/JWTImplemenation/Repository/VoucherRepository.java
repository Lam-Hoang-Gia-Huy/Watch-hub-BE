package com.example.JWTImplemenation.Repository;

import com.example.JWTImplemenation.Entities.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByCodeAndStatusTrue(String code);
}

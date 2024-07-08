package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.Entities.Voucher;

import java.util.List;
import java.util.Optional;

public interface IVoucherService {
    Optional<Voucher> findByCode(String code);
    Voucher save(Voucher voucher);
    void incrementUsage(Voucher voucher);
    void approveVoucher(Integer id);
    List<Voucher> findAll();
    void deactivateVoucher(Integer id);
    List<Voucher> findAllAvailableVouchers();
}

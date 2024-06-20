package com.example.JWTImplemenation.Service.IService;

import com.example.JWTImplemenation.DTO.WatchDTO;
import com.example.JWTImplemenation.Entities.Watch;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IWatchService {
    ResponseEntity<List<WatchDTO>> findAll();
     ResponseEntity<WatchDTO> findById(Integer id);
    ResponseEntity<WatchDTO> save(Integer userId, Watch watch);
    ResponseEntity<WatchDTO> update(Integer id, Watch watch);
    ResponseEntity<Void> deleteById(Integer id);
    ResponseEntity<WatchDTO> addImagesToWatch(Integer watchId, List<MultipartFile> imageFiles);
    void updateWatchStatus(List<Integer> watchIds, boolean status, boolean isPaid);
}

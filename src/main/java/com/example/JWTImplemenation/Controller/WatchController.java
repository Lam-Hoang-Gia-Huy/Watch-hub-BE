package com.example.JWTImplemenation.Controller;

import com.example.JWTImplemenation.DTO.WatchDTO;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Service.IService.IWatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/watch")
@RequiredArgsConstructor
public class WatchController {

    @Autowired
    private IWatchService iWatchService;

    @GetMapping
    public ResponseEntity<List<WatchDTO>> getAllWatches() {
        return iWatchService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchDTO> getWatchById(@PathVariable Integer id) {
        return iWatchService.findById(id);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<WatchDTO> createWatchForUser(@PathVariable Integer userId, @RequestBody Watch watch) {
        return iWatchService.save(userId, watch);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WatchDTO> updateWatch(@PathVariable Integer id, @RequestBody Watch watch) {
        return iWatchService.update(id, watch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatch(@PathVariable Integer id) {
        return iWatchService.deleteById(id);
    }

    @PostMapping("/{watchId}/images")
    public ResponseEntity<WatchDTO> addImagesToWatch(@PathVariable Integer watchId, @RequestParam("imageFiles") List<MultipartFile> imageFiles) {
        return iWatchService.addImagesToWatch(watchId, imageFiles);
    }
}

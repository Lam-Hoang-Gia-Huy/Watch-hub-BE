package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.WatchDTO;
import com.example.JWTImplemenation.Entities.ImageUrl;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Repository.WatchRespository;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Service.IService.IImageService;
import com.example.JWTImplemenation.Service.IService.IWatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchService implements IWatchService {

    @Autowired
    private WatchRespository watchRepository;
    @Autowired
    private IImageService imageService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<List<WatchDTO>> findAll() {
        List<Watch> watches = watchRepository.findAll();
        return ResponseEntity.ok(convertToDTOList(watches));
    }

    @Override
    public ResponseEntity<WatchDTO> findById(Integer id) {
        Optional<Watch> watch = watchRepository.findById(id);
        return watch.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<WatchDTO> save(Integer userId, Watch watch) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            watch.setStatus(true);
            watch.setPaid(false);
            watch.setUser(user);
            Watch savedWatch = watchRepository.save(watch);
            return ResponseEntity.ok(convertToDTO(savedWatch));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<WatchDTO> update(Integer id, Watch watch) {
        if (watchRepository.existsById(id)) {
            watch.setId(id);
            Watch updatedWatch = watchRepository.save(watch);
            return ResponseEntity.ok(convertToDTO(updatedWatch));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteById(Integer id) {
        Optional<Watch> optionalWatch = watchRepository.findById(id);
        if (optionalWatch.isPresent()) {
            Watch watch = optionalWatch.get();
            watch.setStatus(false); // Set status to false (0)
            watchRepository.save(watch);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @Override
    public ResponseEntity<WatchDTO> addImagesToWatch(Integer watchId, List<MultipartFile> imageFiles) {
        Optional<Watch> optionalWatch = watchRepository.findById(watchId);
        if (optionalWatch.isPresent()) {
            Watch watch = optionalWatch.get();
            List<ImageUrl> imageUrls = new ArrayList<>();
            for (MultipartFile imageFile : imageFiles) {
                String imageUrl = imageService.uploadImage(imageFile);
                if (imageUrl != null) {
                    ImageUrl image = new ImageUrl();
                    image.setImageUrl(imageUrl);
                    image.setWatch(watch);
                    imageUrls.add(image);
                }
            }
            if (watch.getImageUrl() == null) {
                watch.setImageUrl(new ArrayList<>());
            }
            watch.getImageUrl().addAll(imageUrls);
            Watch updatedWatch = watchRepository.save(watch);
            return ResponseEntity.ok(convertToDTO(updatedWatch));
        }
        return ResponseEntity.notFound().build();
    }


    private WatchDTO convertToDTO(Watch watch) {
        WatchDTO watchDTO = new WatchDTO();
        watchDTO.setId(watch.getId());
        watchDTO.setName(watch.getName());
        watchDTO.setBrand(watch.getBrand());
        watchDTO.setDescription(watch.getDescription());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setPaid(watch.isPaid());
        watchDTO.setStatus(watch.isStatus());
        watchDTO.setCreatedDate(watch.getCreatedDate());
        watchDTO.setCreatedDate(watch.getCreatedDate());
        if (watch.getImageUrl() != null) {
            List<String> imageUrls = watch.getImageUrl()
                    .stream()
                    .map(ImageUrl::getImageUrl)
                    .collect(Collectors.toList());
            watchDTO.setImageUrl(imageUrls);
        }
        if (watch.getAppraisal() != null) {
            watchDTO.setAppraisalId(watch.getAppraisal().getId());
        }
        watchDTO.setSellerId(watch.getUser().getId()); // Set sellerId
        return watchDTO;
    }


    private List<WatchDTO> convertToDTOList(List<Watch> watches) {
        return watches.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    @Override
    public void updateWatchStatus(List<Integer> watchIds, boolean status, boolean isPaid) {
        for (Integer id : watchIds) {
            Optional<Watch> watchOptional = watchRepository.findById(id);
            if (watchOptional.isPresent()) {
                Watch watch = watchOptional.get();
                watch.setStatus(status);
                watch.setPaid(isPaid);
                watchRepository.save(watch);
            }
        }
    }
}

package com.example.JWTImplemenation.Service;

import com.example.JWTImplemenation.DTO.AppraisalDTO;
import com.example.JWTImplemenation.Entities.Appraisal;
import com.example.JWTImplemenation.Entities.User;
import com.example.JWTImplemenation.Entities.Watch;
import com.example.JWTImplemenation.Repository.AppraisalRepository;
import com.example.JWTImplemenation.Repository.WatchRespository;
import com.example.JWTImplemenation.Repository.UserRepository;
import com.example.JWTImplemenation.Service.IService.IAppraisalService;
import com.example.JWTImplemenation.Util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppraisalService implements IAppraisalService {

    @Autowired
    private AppraisalRepository appraisalRepository;

    @Autowired
    private WatchRespository watchRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public ResponseEntity<List<AppraisalDTO>> findAll() {
        List<Appraisal> appraisals = appraisalRepository.findAll();
        return ResponseEntity.ok(convertToDTOList(appraisals));
    }

    @Override
    public ResponseEntity<AppraisalDTO> findById(Integer id) {
        Optional<Appraisal> appraisal = appraisalRepository.findById(id);
        return appraisal.map(value -> ResponseEntity.ok(convertToDTO(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<AppraisalDTO> save( AppraisalDTO appraisalDTO) {
        Optional<Watch> optionalWatch = watchRepository.findById(appraisalDTO.getWatchId());
        if (optionalWatch.isPresent()) {
            Watch watch = optionalWatch.get();

            // Fetch the appraiser using appraiserId from the DTO
            Optional<User> optionalUser = userRepository.findById(appraisalDTO.getAppraiserId());
            if (!optionalUser.isPresent()) {
                return ResponseEntity.badRequest().build(); // or any appropriate error response
            }
            User appraiser = optionalUser.get();

            // Convert DTO to entity
            Appraisal appraisal = convertToEntity(appraisalDTO);
            appraisal.setWatch(watch);
            appraisal.setAppraiser(appraiser);

            Appraisal savedAppraisal = appraisalRepository.save(appraisal);

            // Update the price of the associated watch
            watch.setPrice(savedAppraisal.getValue());
            watchRepository.save(watch);

            return ResponseEntity.ok(convertToDTO(savedAppraisal));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<AppraisalDTO> update(Integer id, Appraisal appraisal) {
        if (appraisalRepository.existsById(id)) {
            appraisal.setId(id);
            Appraisal updatedAppraisal = appraisalRepository.save(appraisal);
            return ResponseEntity.ok(convertToDTO(updatedAppraisal));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteById(Integer id) {
        if (appraisalRepository.existsById(id)) {
            appraisalRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private AppraisalDTO convertToDTO(Appraisal appraisal) {
        // Ensure appraiser is not null before accessing its ID
        Integer appraiserId = (appraisal.getAppraiser() != null) ? appraisal.getAppraiser().getId() : null;
        return AppraisalDTO.builder()
                .id(appraisal.getId())
                .comments(appraisal.getComments())
                .value(appraisal.getValue())

                .material(appraisal.getMaterial())
                .thickness(appraisal.getThickness())
                .dial(appraisal.getDial())
                .movement(appraisal.getMovement())
                .crystal(appraisal.getCrystal())
                .bracket(appraisal.getBracket())
                .buckle(appraisal.getBuckle())
                .watchId(appraisal.getId())
                .appraiserId(appraiserId) // Ensure this doesn't cause an issue
                .build();
    }

    private List<AppraisalDTO> convertToDTOList(List<Appraisal> appraisals) {
        return appraisals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    private Appraisal convertToEntity(AppraisalDTO appraisalDTO) {
        return Appraisal.builder()
                .id(appraisalDTO.getId())
                .comments(appraisalDTO.getComments())
                .value(appraisalDTO.getValue())

                .material(appraisalDTO.getMaterial())
                .thickness(appraisalDTO.getThickness())
                .dial(appraisalDTO.getDial())
                .movement(appraisalDTO.getMovement())
                .crystal(appraisalDTO.getCrystal())
                .bracket(appraisalDTO.getBracket())
                .buckle(appraisalDTO.getBuckle())
                .build();
    }
}

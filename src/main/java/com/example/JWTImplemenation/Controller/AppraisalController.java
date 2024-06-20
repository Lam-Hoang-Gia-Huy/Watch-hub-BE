package com.example.JWTImplemenation.Controller;

import com.example.JWTImplemenation.DTO.AppraisalDTO;
import com.example.JWTImplemenation.Entities.Appraisal;
import com.example.JWTImplemenation.Service.IService.IAppraisalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appraisal")
public class AppraisalController {

    @Autowired
    private IAppraisalService appraisalService;

    @GetMapping
    public ResponseEntity<List<AppraisalDTO>> getAllAppraisals() {
        return appraisalService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppraisalDTO> getAppraisalById(@PathVariable Integer id) {
        return appraisalService.findById(id);
    }

    @PostMapping("/watch")
    public ResponseEntity<AppraisalDTO> createAppraisal(@RequestBody AppraisalDTO appraisalDTO) {
        return appraisalService.save(appraisalDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppraisalDTO> updateAppraisal(@PathVariable Integer id, @RequestBody Appraisal appraisal) {
        return appraisalService.update(id, appraisal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppraisal(@PathVariable Integer id) {
        return appraisalService.deleteById(id);
    }
}

package com.example.JWTImplemenation.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class ChatStartRequest {
    private Integer watchId;
    private Integer userId;
    private Integer appraiserId;
    public Integer getWatchId() {
        return watchId;
    }

    public void setWatchId(Integer watchId) {
        this.watchId = watchId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAppraiserId() {
        return appraiserId;
    }

    public void setAppraiserId(Integer appraiserId) {
        this.appraiserId = appraiserId;
    }
}

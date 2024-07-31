package com.example.curtaincall.dto;

import com.example.curtaincall.domain.RecentCallLog;
import lombok.Builder;

import java.util.Date;

public record RequestRecentCallLogDTO(boolean isMissedCall, String nickName, Date recentCallDate) {

    @Builder
    public RequestRecentCallLogDTO(boolean isMissedCall, String nickName, Date recentCallDate){
        this.isMissedCall = isMissedCall;
        this.nickName = nickName;
        this.recentCallDate = recentCallDate;
    }

    public RecentCallLog toEntity(String phoneNumber){
        return RecentCallLog.builder()
                .recentCallDate(this.recentCallDate)
                .isMissedCall(this.isMissedCall)
                .nickName(this.nickName)
                .phoneNumber(phoneNumber).build();
    }
}

package com.example.curtaincall.dto;

import com.example.curtaincall.domain.RecentCallLog;
import com.example.curtaincall.domain.User;
import lombok.Builder;

import java.util.Date;

public record ResponseRecentCallLogDTO(boolean isMissedCall, String phoneNumber,String nickName, Date recentCallDate) {
    @Builder
    public ResponseRecentCallLogDTO(boolean isMissedCall, String phoneNumber,String nickName, Date recentCallDate){
        this.isMissedCall = isMissedCall;
        this.nickName = nickName;
        this.recentCallDate = recentCallDate;
        this.phoneNumber = phoneNumber;
    }

    public RecentCallLog toEntity(){
        return RecentCallLog.builder()
                .recentCallDate(this.recentCallDate)
                .isMissedCall(this.isMissedCall)
                .nickName(this.nickName)
                .phoneNumber(this.phoneNumber).build();
    }
}

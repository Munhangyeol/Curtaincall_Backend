package com.example.curtaincall.dto.request;

import com.example.curtaincall.domain.RecentCallLog;
import lombok.Builder;

import java.util.Date;
import java.util.List;

public record RequestRecentCallLogDTO(List<String> phoneNumbers) {

    @Builder
    public RequestRecentCallLogDTO(List<String> phoneNumbers){
        this.phoneNumbers = phoneNumbers;
    }


}

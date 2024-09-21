package com.example.curtaincall.dto.response;

import com.example.curtaincall.domain.RecentCallLog;
import com.example.curtaincall.domain.User;
import lombok.Builder;

import java.util.Date;
import java.util.List;

public record ResponseRecentCallLogDTO(List<CallLogInfo> callLogInfos) {
    @Builder
    public ResponseRecentCallLogDTO(List<CallLogInfo> callLogInfos) {
        this.callLogInfos = callLogInfos;
    }

    public static record CallLogInfo(String nickname, String phoneNumber) {
        @Builder
        public CallLogInfo(String nickname,String phoneNumber) {
            this.nickname = nickname;
            this.phoneNumber = phoneNumber;
            // 빌더 패턴을 사용한 생성자
        }
    }
}

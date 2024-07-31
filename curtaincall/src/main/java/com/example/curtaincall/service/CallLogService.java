package com.example.curtaincall.service;

import com.example.curtaincall.RecentCallLogRepository;
import com.example.curtaincall.domain.RecentCallLog;
import com.example.curtaincall.dto.RequestRecentCallLogDTO;
import com.example.curtaincall.dto.ResponseRecentCallLogDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CallLogService {
    private final RecentCallLogRepository recentCallLogRepository;

    public CallLogService(RecentCallLogRepository recentCallLogRepository) {
        this.recentCallLogRepository = recentCallLogRepository;
    }
    public void saveRecentCallLog(RequestRecentCallLogDTO recentCallLogDTO, String phoneNumber){
        recentCallLogRepository.save(recentCallLogDTO.toEntity(phoneNumber));
    }
    public List<ResponseRecentCallLogDTO> getRecenCallLogs(String phoneNumber){
       List<RecentCallLog> recentCallLogs=recentCallLogRepository.findByPhoneNumber(phoneNumber).orElseThrow(()
       ->new RuntimeException("해당 전화번호가 recentCallRepo에 없습니다"+" "+phoneNumber));
       return recentCallLogs.stream().map(recentCallLog ->
               ResponseRecentCallLogDTO.builder().isMissedCall(recentCallLog.isMissedCall())
                       .recentCallDate(recentCallLog.getRecentCallDate())
                       .nickName(recentCallLog.getNickName())
                       .build()).collect(Collectors.toList());

    }
}

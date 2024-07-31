package com.example.curtaincall.controller;

import com.example.curtaincall.domain.RecentCallLog;
import com.example.curtaincall.dto.RequestRecentCallLogDTO;
import com.example.curtaincall.dto.ResponseRecentCallLogDTO;
import com.example.curtaincall.service.CallLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CallController {
    private final CallLogService callLogService;

    public CallController(CallLogService callLogService) {
        this.callLogService = callLogService;
    }

    @ResponseBody
    @PostMapping("/main/recentCallHistory")
    public String saveRecentCallLog(@RequestParam("phoneNumber") String phoneNumber,
                                    @RequestBody RequestRecentCallLogDTO recentCallLogDTO){
        callLogService.saveRecentCallLog(recentCallLogDTO,phoneNumber);

        return "sucessfull save recent call log";
    }
    @ResponseBody
    @GetMapping("/main/recentCallHistory")
    public ResponseEntity<List<ResponseRecentCallLogDTO>> getRecentCallLog(@RequestParam("phoneNumber") String phoneNumber
                                                                           ){
        return ResponseEntity.ok(callLogService.getRecenCallLogs(phoneNumber));

    }
}

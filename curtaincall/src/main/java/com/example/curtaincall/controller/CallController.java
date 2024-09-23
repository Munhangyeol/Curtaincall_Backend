package com.example.curtaincall.controller;

import com.example.curtaincall.dto.request.RequestRecentCallLogDTO;
import com.example.curtaincall.dto.response.ResponseRecentCallLogDTO;
import com.example.curtaincall.global.TokenManager;
import com.example.curtaincall.service.CallLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CallController {
    private final CallLogService callLogService;
    private final TokenManager tokenManager;

    public CallController(CallLogService callLogService, TokenManager tokenManager) {
        this.callLogService = callLogService;
        this.tokenManager = tokenManager;
    }


    @ResponseBody
    @PostMapping("/main/recentCallHistory")
    public ResponseEntity<ResponseRecentCallLogDTO> getRecentCallLog(HttpServletRequest request,
                                                                        @RequestBody RequestRecentCallLogDTO recentCallLogDTO   ){
        return ResponseEntity.ok(callLogService.findRecentCallContact(tokenManager.getPhoneNumberByToken(request),
                recentCallLogDTO));

    }
}

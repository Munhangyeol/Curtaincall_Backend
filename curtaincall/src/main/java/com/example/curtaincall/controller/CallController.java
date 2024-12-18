package com.example.curtaincall.controller;

import com.example.curtaincall.dto.request.RequestRecentCallLogDTO;
import com.example.curtaincall.dto.response.ResponseRecentCallLogDTO;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import com.example.curtaincall.service.CallLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CallController {
    private final CallLogService callLogService;
    public CallController(CallLogService callLogService) {
        this.callLogService = callLogService;
    }
    @ResponseBody
    @PostMapping("/main/recentCallHistory")
    public ResponseEntity<ResponseRecentCallLogDTO> getRecentCallLog(@AuthenticationPrincipal CustomUserDetails
            userDetails,
                                                                     @RequestBody RequestRecentCallLogDTO recentCallLogDTO   ){
        return ResponseEntity.ok(callLogService.findRecentCallContact(userDetails.getPhoneNumber(),
                recentCallLogDTO));

    }
}

package com.example.curtaincall.controller;

import com.example.curtaincall.service.CurtainCallMessageService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final CurtainCallMessageService curtainCallMessageService;

    @GetMapping("authorization/send-one")
    public SingleMessageSentResponse sendOne(@RequestParam("phoneNumber")String phoneNumber) {
        return curtainCallMessageService.makeMessageResponse(phoneNumber);
    }
    @GetMapping("authorization/configNumber")
    public ResponseEntity<Boolean> configNumber(@RequestParam("phoneNumber")String phoneNumber,
                                                @RequestParam("phoneNumber") String configNumber) {
        return ResponseEntity.ok( curtainCallMessageService.configNumber(phoneNumber, configNumber));
    }
}

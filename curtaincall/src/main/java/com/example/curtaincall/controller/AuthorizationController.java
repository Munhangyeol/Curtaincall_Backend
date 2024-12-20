package com.example.curtaincall.controller;

import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.exception.UserAlreadyExistsException;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import com.example.curtaincall.repository.UserRepository;
import com.example.curtaincall.service.AuthorizaionService;
import com.example.curtaincall.service.CurtainCallMessageService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final CurtainCallMessageService curtainCallMessageService;
    private final AuthorizaionService authorizaionService;
    private final UserRepository userRepository;
    private final SecretkeyManager manager;

    @GetMapping("/health-check")
    public ResponseEntity<Void> checkHealthStatus() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("authorization/send-one")
    public SingleMessageSentResponse sendOne(@RequestBody Map<String,String> phoneNumberMap) {
       String phoneNumber= phoneNumberMap.get("phoneNumber");
        if(userRepository.findByPhoneNumber(manager.encrypt(phoneNumber)).isPresent()){
            throw new UserAlreadyExistsException("This user is already existed");
        }
        return curtainCallMessageService.makeMessageResponse(phoneNumber);
    }
    @PostMapping("authorization/configNumber")
    public ResponseEntity<Boolean> configNumber(@RequestBody Map<String,String> phoneNumberMap,
                                                @RequestParam("configNumber") String configNumber) {
        Boolean configResult = curtainCallMessageService.configNumber(phoneNumberMap.get("phoneNumber"), configNumber);
        return ResponseEntity.ok(configResult);
    }
    @GetMapping("authorization/configUser")
    public ResponseEntity<Boolean> configUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok((authorizaionService.isUser(userDetails)));
    }
}

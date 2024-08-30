package com.example.curtaincall.controller;

import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.ResponseAuthorizationDTO;
import com.example.curtaincall.global.exception.UserAlreadyExistsException;
import com.example.curtaincall.global.exception.UserNotfoundException;
import com.example.curtaincall.repository.UserRepository;
import com.example.curtaincall.service.AuthorizaionService;
import com.example.curtaincall.service.CurtainCallMessageService;
import com.example.curtaincall.service.UserService;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {

    private final CurtainCallMessageService curtainCallMessageService;
    private final AuthorizaionService authorizaionService;
    private final UserRepository userRepository;



    @GetMapping("authorization/send-one")
    public SingleMessageSentResponse sendOne(@RequestParam("phoneNumber")String phoneNumber) {
        if(userRepository.findByPhoneNumber(phoneNumber).isPresent()){
            throw new UserAlreadyExistsException("This user is already existed");
        }
        return curtainCallMessageService.makeMessageResponse(phoneNumber);
    }
    @GetMapping("authorization/configNumber")
    public ResponseEntity<Boolean> configNumber(@RequestParam("phoneNumber")String phoneNumber,
                                                @RequestParam("configNumber") String configNumber) {
        Boolean configResult = curtainCallMessageService.configNumber(phoneNumber, configNumber);

        return ResponseEntity.ok(configResult);
    }
    @GetMapping("authorization/configUser")
    public ResponseEntity<Boolean> configUser(@RequestParam("phoneNumber")String phoneNumber) {
//        authorizaionService.isUser(phoneNumber);
        return ResponseEntity.ok((authorizaionService.isUser(phoneNumber)));
    }


}

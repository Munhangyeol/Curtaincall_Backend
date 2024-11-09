package com.example.curtaincall.controller;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestRemovedNumberInPhoneBookDTO;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.dto.response.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.response.ResponseUserDTO;
import com.example.curtaincall.global.exception.UserAlreadyExistsException;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import com.example.curtaincall.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @PostMapping("/main/user")
    public ResponseEntity<String> saveUser(@RequestBody RequestUserDTO requestUserDTO){
        String token=userService.saveUser(requestUserDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/main/user/phoneAddressBookInfo")
    public String saveUserPhoneAdderssBook(@RequestBody  Map<String, List<Contact>> requestPhoneBookDTO){
        userService.saveUserPhoneBooks(requestPhoneBookDTO);
        return "Successfull Save User Phonebook";
    }


    @GetMapping("/main/user")
    public ResponseEntity<ResponseUserDTO> getUser(@AuthenticationPrincipal CustomUserDetails userDetails){
        ResponseUserDTO responseUserDTO=userService.findUser(userDetails);
        return ResponseEntity.ok(responseUserDTO);
    }
    @GetMapping("/main/user/phoneAddressBookInfo")
    public ResponseEntity<ResponsePhoneBookDTO> getPhoneBook(@AuthenticationPrincipal CustomUserDetails userDetails){
        ResponsePhoneBookDTO responsePhoneBookDTO=userService.findPhoneBook(userDetails);
        return ResponseEntity.ok(responsePhoneBookDTO);
    }

    @PutMapping("/main/user")
    public String changeUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestBody RequestUserDTO requestUserDTO){
        userService.updateUser(requestUserDTO,userDetails);
        return "Successfull update user!";
    }

    @PutMapping("/main/user/phoneAddressBookInfo")
    public String changeUserPhoneBook(@AuthenticationPrincipal CustomUserDetails userDetails,
                                 @RequestBody Map<String,Contact> putRequestPhonebookDTO){
            userService.updatePhoneBook(putRequestPhonebookDTO,
                    userDetails);
        return "Successfull update AddressBook!";
    }


    @PostMapping("/main/user/setOff")
    public ResponseEntity<List<ResponseUserDTO>> getPhoneBookUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestBody Map<String,String> userPhoneBookNumberMap){
        return ResponseEntity.ok(userService.getUserInPhoneBookAndSetOff(userDetails, userPhoneBookNumberMap.get("userPhoneBookNumber")));
    }
    @PostMapping("main/user/phoneAddressBookInfo/remove")
    public String removeNumberInPhoneBook(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestBody RequestRemovedNumberInPhoneBookDTO removedNumberDTO){
        userService.deleteContactInPhoneNumber(userDetails,removedNumberDTO);
        return "성공적으로 삭제 되었습니다.";
    }


    @GetMapping("/main/user/rollback")
    public ResponseEntity<ResponsePhoneBookDTO> getPhoneBookWithRollback() {
        CustomUserDetails userDetails= (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ResponsePhoneBookDTO responsePhoneBookDTO = userService.getPhoneBookWithSetAllOff(userDetails);
        return ResponseEntity.ok(responsePhoneBookDTO);
    }

    @GetMapping("/main/user/setAllOn")
    public String setAllOnPhoneBook(@AuthenticationPrincipal CustomUserDetails userDetails) {
    userService.setAllOnPhoneBook(userDetails);
        return "커튼콜 기능 일괄 활성화 되었습니다";
    }

}

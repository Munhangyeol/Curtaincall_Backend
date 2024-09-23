package com.example.curtaincall.controller;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestRemovedNumberInPhoneBookDTO;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.dto.response.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.response.ResponseUserDTO;
import com.example.curtaincall.global.TokenManager;
import com.example.curtaincall.global.auth.jwt.JwtUtils;
import com.example.curtaincall.global.userDetail.CustomUserDetailService;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import com.example.curtaincall.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenManager tokenManager;
    public UserController(UserService userService,TokenManager tokenManager) {
        this.userService = userService;
        this.tokenManager = tokenManager;
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
    public ResponseEntity<ResponseUserDTO> getUser(HttpServletRequest request){
        ResponseUserDTO responseUserDTO=userService.findUserByPhoneNumber(tokenManager.getPhoneNumberByToken(request));
        return ResponseEntity.ok(responseUserDTO);
    }

    @GetMapping("/main/user/phoneAddressBookInfo")
    public ResponseEntity<ResponsePhoneBookDTO> getPhoneBook(HttpServletRequest request){
        ResponsePhoneBookDTO responsePhoneBookDTO=userService.findPhoneBookByPhoneNumber(tokenManager.getPhoneNumberByToken(request));
        return ResponseEntity.ok(responsePhoneBookDTO);
    }

    @PutMapping("/main/user")
    public String changeUser(HttpServletRequest request,
                                 @RequestBody RequestUserDTO requestUserDTO){
        userService.updateUser(requestUserDTO,tokenManager.getPhoneNumberByToken(request));
        return "Successfull update user!";
    }

    @PutMapping("/main/user/phoneAddressBookInfo")
    public String changeUserPhoneBook(HttpServletRequest request,
                                 @RequestBody Map<String,Contact> putRequestPhonebookDTO){
            userService.updatePhoneBook(putRequestPhonebookDTO,
                    tokenManager.getPhoneNumberByToken(request));
        return "Successfull update AddressBook!";
    }


    @GetMapping("/main/user/setOff")
    public ResponseEntity<List<ResponseUserDTO>> getPhoneBookUser(HttpServletRequest request,
                                                           @RequestBody Map<String,String> userPhoneBookNumberMap){
        String userPhoneNumber=tokenManager.getPhoneNumberByToken(request);
        return ResponseEntity.ok(userService.getUserInPhoneBookAndSetOff(userPhoneNumber, userPhoneBookNumberMap.get("userPhoneBookNumber")));
    }
    @PostMapping("main/user/phoneAddressBookInfo/remove")
    public String removeNumberInPhoneBook(HttpServletRequest request,
                                          @RequestBody RequestRemovedNumberInPhoneBookDTO removedNumberDTO){
        userService.deleteContaceInPhoneNumber(tokenManager.getPhoneNumberByToken(request),removedNumberDTO);
        return "성공적으로 삭제 되었습니다.";
    }




    @GetMapping("/main/user/rollback")
    public ResponseEntity<ResponsePhoneBookDTO> getPhoneBookWithRollback(HttpServletRequest request) {

        ResponsePhoneBookDTO responsePhoneBookDTO = userService.getPhoneBookWithRollback(tokenManager.getPhoneNumberByToken(request));
        return ResponseEntity.ok(responsePhoneBookDTO);
    }

    @GetMapping("/main/user/setAllOn")
    public String setAllOnPhoneBook(HttpServletRequest request) {
    userService.setAllOnPhoneBook(tokenManager.getPhoneNumberByToken(request));
        return "커튼콜 기능 일괄 활성화 되었습니다";
    }

}

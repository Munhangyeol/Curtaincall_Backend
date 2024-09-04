package com.example.curtaincall.controller;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.RequestUserDTO;
import com.example.curtaincall.dto.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.ResponseUserDTO;
import com.example.curtaincall.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
//@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/main/user")
    public String saveUser(@RequestBody RequestUserDTO requestUserDTO){
        userService.saveUser(requestUserDTO);
        return "Successfull Save User";
    }
    @ResponseBody
    @PostMapping("/main/user/phoneAddressBookInfo")
    public String saveUserPhoneAdderssBook(@RequestBody  Map<String, List<Contact>> requestPhoneBookDTO){
        userService.saveUserPhoneBooks(requestPhoneBookDTO);
        return "Successfull Save User Phonebook";
    }

    @GetMapping("/main/user")
    public ResponseEntity<ResponseUserDTO> getUser(@RequestParam("phoneNumber") String phoneNumber){
        ResponseUserDTO responseUserDTO = userService.findUserByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(responseUserDTO);
    }

    @GetMapping("/main/user/phoneAddressBookInfo")
    public ResponseEntity<ResponsePhoneBookDTO> getPhoneBook(@RequestParam("phoneNumber")String phoneNumber){
        ResponsePhoneBookDTO responsePhoneBookDTO = userService.findPhoneBookByPhoneNumber(phoneNumber);
        return ResponseEntity.ok(responsePhoneBookDTO);
    }
    @ResponseBody
    @PutMapping("/main/user")
    public String changeUser(@RequestParam("prePhoneNumber") String prePhoneNumber,
                                 @RequestBody RequestUserDTO requestUserDTO){
        userService.updateUser(requestUserDTO,prePhoneNumber);
        return "Successfull update user!";
    }
    @ResponseBody
    @PutMapping("/main/user/phoneAddressBookInfo")
    public String changeUserPhoneBook(@RequestParam("prePhoneNumber") String prePhoneNumber,
                                 @RequestBody Map<String,Contact> putRequestPhonebookDTO){
        userService.updatePhoneBook(putRequestPhonebookDTO,prePhoneNumber);
        return "Successfull update AddressBook!";
    }
    @ResponseBody
    @GetMapping("/main/user/calling")
    public ResponseEntity<List<Contact>> getCurrentUserInfo(@RequestParam("userPhoneNumber")String userPhoneNumber,
                                     @RequestParam("postPhoneNumber")String postPhoneNumber){

        return ResponseEntity.ok(userService.getCurrentUserInfo(userPhoneNumber, postPhoneNumber));
    }

    @ResponseBody
    @GetMapping("/main/user/rollback")
    public ResponseEntity<ResponsePhoneBookDTO> getPhoneBookWithRollback(@RequestParam("phoneNumber")String phoneNumber) {
        ResponsePhoneBookDTO responsePhoneBookDTO = userService.getPhoneBookWithRollback(phoneNumber);
        return ResponseEntity.ok(responsePhoneBookDTO);
    }
    @ResponseBody
    @GetMapping("/main/user/setAllOn")
    public String setAllOnPhoneBook(@RequestParam("phoneNumber")String phoneNumber) {
    userService.setAllOnPhoneBook(phoneNumber);
        return "커튼콜 기능 일괄 활성화 되었습니다";
    }

}

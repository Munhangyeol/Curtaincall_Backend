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
        System.out.println(requestPhoneBookDTO);
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
    public String changeUser(@RequestBody RequestUserDTO requestUserDTO){
        userService.updateUser(requestUserDTO);
        return "Successfull update user!";
    }
    @ResponseBody
    @PutMapping("/main/user/phoneAddressBookInfo")
    public String changeUserPhoneBook(@RequestParam("prePhoneNumber") String prePhoneNumber,
                                 @RequestBody Map<String,Contact> putRequestPhonebookDTO){
        userService.updatePhoneBook(putRequestPhonebookDTO,prePhoneNumber);
        return "Successfull update AddressBook!";
    }

}

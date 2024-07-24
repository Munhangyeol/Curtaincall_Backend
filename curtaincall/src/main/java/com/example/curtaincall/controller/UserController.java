package com.example.curtaincall.controller;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.RequestPhoneBookDTO;
import com.example.curtaincall.dto.RequestUserDTO;
import com.example.curtaincall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String saveUser(@RequestBody Map<String, List<Contact>> requestPhoneBookDTO){
        System.out.println(requestPhoneBookDTO.keySet());
        userService.saveUserPhoneBooks(requestPhoneBookDTO);
        return "Successfull Save User Phonebook";
    }
}

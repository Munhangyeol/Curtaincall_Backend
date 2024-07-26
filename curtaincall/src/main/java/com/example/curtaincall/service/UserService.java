package com.example.curtaincall.service;

import com.example.curtaincall.PhoneBookRepository;
import com.example.curtaincall.UserRepository;
import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.RequestUserDTO;
import com.example.curtaincall.dto.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.ResponseUserDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PhoneBookRepository phoneBookRepository;

    public UserService(UserRepository userRepository, PhoneBookRepository phoneBookRepository) {
        this.userRepository = userRepository;
        this.phoneBookRepository = phoneBookRepository;
    }

    public void saveUser(RequestUserDTO requestUserDTO){
        userRepository.save(requestUserDTO.toEntity());
    }
    public void saveUserPhoneBooks(Map<String, List<Contact>> requestPhoneBookDTO){
        for (String stringListEntry : requestPhoneBookDTO.keySet()) {
            for (User user : userRepository.findAll()) {
                System.out.println(user.getPhoneNumber());
            }
            User user = userRepository.findByPhoneNumber(stringListEntry).orElseThrow(
                    () -> new RuntimeException("This phonenumber is not in this repostiory"));
            requestPhoneBookDTO.get(stringListEntry).forEach(
                    contact -> {
                        phoneBookRepository.save(contact.toEntity(user));}
            );
        }
    }
    public ResponseUserDTO findUserByPhoneNumber(String phoneNumber){
        User user=userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                () -> new RuntimeException("This user is not in this repository")
        );
        return ResponseUserDTO.builder().nickName(user.getNickName())
                .isCurtainCallOnAndOff(true)
                .build();
    }
    public ResponsePhoneBookDTO findPhoneBookByPhoneNumber(String phoneNumber){

        return new ResponsePhoneBookDTO();
    }

}

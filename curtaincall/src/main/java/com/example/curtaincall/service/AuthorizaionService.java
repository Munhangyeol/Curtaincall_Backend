package com.example.curtaincall.service;

import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.ResponseUserDTO;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.exception.AuthorizationException;
import com.example.curtaincall.global.exception.UserNotfoundException;
import com.example.curtaincall.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizaionService {
    private final UserRepository userRepository;
    private final SecretkeyManager secretkeyManager;


    public AuthorizaionService(UserRepository userRepository, SecretkeyManager secretkeyManager) {
        this.userRepository = userRepository;
        this.secretkeyManager = secretkeyManager;
    }
    public Boolean isUser(String phoneNumber){
        Optional<User> user = userRepository.findByPhoneNumber(secretkeyManager.encrypt(phoneNumber));
        return user.isPresent();

    }
}

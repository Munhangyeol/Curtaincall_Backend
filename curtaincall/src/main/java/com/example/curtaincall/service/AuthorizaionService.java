package com.example.curtaincall.service;

import com.example.curtaincall.domain.User;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizaionService {
    private final UserRepository userRepository;


    public AuthorizaionService(UserRepository userRepository, SecretkeyManager secretkeyManager) {
        this.userRepository = userRepository;
    }
    public Boolean isUser(String phoneNumber){
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        return user.isPresent();
    }
}

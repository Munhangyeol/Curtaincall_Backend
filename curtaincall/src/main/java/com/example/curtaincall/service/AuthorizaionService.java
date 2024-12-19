package com.example.curtaincall.service;

import com.example.curtaincall.domain.User;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import com.example.curtaincall.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizaionService {
    private final UserRepository userRepository;

    public AuthorizaionService(UserRepository userRepository, SecretkeyManager secretkeyManager) {
        this.userRepository = userRepository;
    }
    public Boolean isUser(CustomUserDetails userDetails){
        Optional<User> user = userRepository.findById(userDetails.getId());
        return user.isPresent();
    }
}

package com.example.curtaincall.service;

import com.example.curtaincall.PhoneBookRepository;
import com.example.curtaincall.UserRepository;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.RequestUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

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
                    () -> new RuntimeException("This phonenumber is not this repostiory"));
            requestPhoneBookDTO.get(stringListEntry).forEach(
                    contact -> {
                        phoneBookRepository.save(contact.toEntity(user));}
            );
        }

    }
}

package com.example.curtaincall.service;

import com.example.curtaincall.PhoneBookRepository;
import com.example.curtaincall.UserRepository;
import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.RequestUserDTO;
import com.example.curtaincall.dto.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.ResponseUserDTO;
import com.example.curtaincall.exception.EncryptException;
import com.example.curtaincall.global.SecretkeyManager;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PhoneBookRepository phoneBookRepository;
    private final SecretkeyManager secretkeyManager;

    public UserService(UserRepository userRepository, PhoneBookRepository phoneBookRepository, SecretkeyManager secretkeyManager) {
        this.userRepository = userRepository;
        this.phoneBookRepository = phoneBookRepository;
        this.secretkeyManager = secretkeyManager;
    }
    public void saveUser(RequestUserDTO requestUserDTO) {
        userRepository.save(requestUserDTO.toEntity(secretkeyManager.encrypt(requestUserDTO.phoneNumber())));
    }
    public void saveUserPhoneBooks(Map<String, List<Contact>> requestPhoneBookDTO){
        for (String stringListEntry : requestPhoneBookDTO.keySet()) {
            User user = userRepository.findByPhoneNumber(stringListEntry).orElseThrow(
                    () -> new RuntimeException("This phonenumber is not in this repostiory"));
            requestPhoneBookDTO.get(stringListEntry).forEach(
                    contact -> phoneBookRepository.save(contact.toEntity(user,secretkeyManager.encrypt(
                            secretkeyManager.encrypt(contact.getPhoneNumber())
                    )))
            );
        }
    }
    public ResponseUserDTO findUserByPhoneNumber(String phoneNumber){
        User user = userRepository.findByPhoneNumber(secretkeyManager.encrypt(phoneNumber)).orElseThrow(
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

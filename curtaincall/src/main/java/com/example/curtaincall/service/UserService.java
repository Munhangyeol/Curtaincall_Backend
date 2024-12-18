package com.example.curtaincall.service;

import com.example.curtaincall.dto.request.RequestRemovedNumberInPhoneBookDTO;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.dto.response.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.response.ResponseUserDTO;
import com.example.curtaincall.global.auth.CurtaincallUserInfo;
import com.example.curtaincall.global.auth.jwt.JwtUtils;
import com.example.curtaincall.global.exception.PhoneBookNotfoundException;
import com.example.curtaincall.global.exception.UserAlreadyExistsException;
import com.example.curtaincall.global.exception.UserNotfoundException;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import com.example.curtaincall.repository.PhoneBookRepository;
import com.example.curtaincall.repository.RecentCallLogRepository;
import com.example.curtaincall.repository.UserRepository;
import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.*;
import com.example.curtaincall.global.SecretkeyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

//TODO UserService에서 phoneBook파트와 분리하기
//TODO  고유값은 Id로

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PhoneBookService phoneBookService;
    private final SecretkeyManager secretkeyManager;
    private final JwtUtils jwtUtils;


    public String saveUser(RequestUserDTO requestUserDTO) {
        if (isNotExistsUser(requestUserDTO)) {
            User user = userRepository.save(requestUserDTO.toEntity(encrypt(requestUserDTO.phoneNumber())));
            CurtaincallUserInfo userInfo = getUserInfo(user);
            return jwtUtils.create(userInfo);
        }
        throw new UserAlreadyExistsException();
    }



    public void saveUserPhoneBooks(Map<String, List<Contact>> requestPhoneBookDTO) {
        for (String phoneNumber : requestPhoneBookDTO.keySet()) {
            User user = userRepository.findByPhoneNumber(encrypt(phoneNumber)).orElseThrow(
                    PhoneBookNotfoundException::new);
            phoneBookService.saveAll(requestPhoneBookDTO.get(phoneNumber),user);
        }
    }

    public void updateUser(RequestUserDTO requestUserDTO, CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(
                UserNotfoundException::new
        );
        updateUserDAO(requestUserDTO, user);

        userRepository.save(user);
    }

    public void updatePhoneBook(Map<String, Contact> putRequestDTO,CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(
                UserNotfoundException::new);
        phoneBookService.update(putRequestDTO,user);
    }
    public void deleteContactInPhoneNumber(CustomUserDetails userDetails,
                                           RequestRemovedNumberInPhoneBookDTO numbers){
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotfoundException::new);
        phoneBookService.deletePhoneNumber(numbers,user);
    }
    public ResponseUserDTO findUser(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(
                UserNotfoundException::new
        );
        return ResponseUserDTO.builder()
                .phoneNumber(decrypt(user.getPhoneNumber()))
                .nickName(user.getNickName())
                .isCurtainCallOnAndOff(user.isCurtainCallOnAndOff())
                .build();
    }
    public ResponsePhoneBookDTO findPhoneBook(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(
                UserNotfoundException::new
        );
        return phoneBookService.findPhoneBook(user);
    }
    public List<ResponseUserDTO> getUserInPhoneBookAndSetOff(CustomUserDetails userDetails,String phoneNumberInPhoneBook){
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotfoundException::new);
        return phoneBookService.getUserInPhoneBookAndSetOff(phoneNumberInPhoneBook, user);

    }
    public ResponsePhoneBookDTO getPhoneBookWithSetAllOff(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotfoundException::new);
        return phoneBookService.getPhoneBookWithSetAllOff(user);
    }
    public void setAllOnPhoneBook(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotfoundException::new);
        phoneBookService.setAllOnPhoneBook(user);
    }
    private  CurtaincallUserInfo getUserInfo(User user) {
        return CurtaincallUserInfo.builder().id(user.getId())
                .phoneNumber(user.getPhoneNumber())
                .isCurtaincall(user.isCurtainCallOnAndOff())
                .role(user.getUserRole())
                .build();
    }
    private boolean isNotExistsUser(RequestUserDTO requestUserDTO) {
        return userRepository.findByPhoneNumber(encrypt(requestUserDTO.phoneNumber())).isEmpty();
    }
    private void updateUserDAO(RequestUserDTO requestUserDTO, User user) {
        if(isChangeUserPhoneNumber(requestUserDTO, user)) {
            user.setPhoneNumber(encrypt(requestUserDTO.phoneNumber()));
        }
        user.setNickName(requestUserDTO.nickName());
        user.setCurtainCallOnAndOff(requestUserDTO.isCurtainCall());
    }
    private  boolean isChangeUserPhoneNumber(RequestUserDTO requestUserDTO, User user) {
        return !requestUserDTO.phoneNumber().equals(user.getPhoneNumber());
    }

    private String encrypt(String plainText) {
        return secretkeyManager.encrypt(plainText);
    }

    private String decrypt(String cipherText) {
        return secretkeyManager.decrypt(cipherText);
    }
}

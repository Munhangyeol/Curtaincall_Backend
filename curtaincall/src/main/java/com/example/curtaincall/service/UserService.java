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
public class UserService {

    private final UserRepository userRepository;
    private final PhoneBookRepository phoneBookRepository;
    private final SecretkeyManager secretkeyManager;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, PhoneBookRepository phoneBookRepository, SecretkeyManager secretkeyManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.phoneBookRepository = phoneBookRepository;
        this.secretkeyManager = secretkeyManager;
        this.jwtUtils = jwtUtils;
    }

    public String saveUser(RequestUserDTO requestUserDTO) {
        if (userRepository.findByPhoneNumber(encrypt(requestUserDTO.phoneNumber())).isEmpty()) {
            User user = userRepository.save(requestUserDTO.toEntity(encrypt(requestUserDTO.phoneNumber())));
            CurtaincallUserInfo userInfo = CurtaincallUserInfo.builder().id(user.getId())
                    .phoneNumber(user.getPhoneNumber())
                    .isCurtaincall(user.isCurtainCallOnAndOff())
                    .role(user.getUserRole())
                    .build();
            return jwtUtils.create(userInfo);
        }
        throw  new UserAlreadyExistsException();
    }
    public void saveUserPhoneBooks(Map<String, List<Contact>> requestPhoneBookDTO) {
        for (String phoneNumber : requestPhoneBookDTO.keySet()) {
            User user = userRepository.findByPhoneNumber(encrypt(phoneNumber)).orElseThrow(
                    PhoneBookNotfoundException::new);
            List<Contact> contacts = requestPhoneBookDTO.get(phoneNumber);
            List<PhoneBook> phoneBooks = contacts.stream()
                    .map(contact -> contact.toEntity(user, encrypt(contact.getPhoneNumber())))
                    .collect(Collectors.toList());
            phoneBookRepository.saveAll(phoneBooks);

            contacts.forEach(contact -> System.out.println("onandoff" + contact.getIsCurtainCallOnAndOff()));
        }
    }

    public void updateUser(RequestUserDTO requestUserDTO, CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(
                UserNotfoundException::new
        );
        user.setNickName(requestUserDTO.nickName());
        user.setCurtainCallOnAndOff(requestUserDTO.isCurtainCall());
        if(!requestUserDTO.phoneNumber().equals(user.getPhoneNumber())) {
            user.setPhoneNumber(encrypt(requestUserDTO.phoneNumber()));
        }
        userRepository.save(user);
    }

    public void updatePhoneBook(Map<String, Contact> putRequestDTO,CustomUserDetails userDetails) {

            User user = userRepository.findById(userDetails.getId()).orElseThrow(
                    UserNotfoundException::new);

        for (String phoneNumber : putRequestDTO.keySet()) {
            List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(encrypt(phoneNumber), user)
                    .orElseThrow(PhoneBookNotfoundException::new);
            for (PhoneBook phoneBook : phoneBooks) {
                Contact contact = putRequestDTO.get(phoneNumber);
                phoneBook.setPhoneNumber(encrypt(contact.getPhoneNumber()));
                phoneBook.setNickName(contact.getName());
                phoneBook.setCurtainCallOnAndOff(contact.getIsCurtainCallOnAndOff());
            }
            phoneBookRepository.saveAll(phoneBooks);
        }
    }
    public void deleteContactInPhoneNumber(CustomUserDetails userDetails,
                                           RequestRemovedNumberInPhoneBookDTO numbers){
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotfoundException::new);

        Arrays.stream(numbers.removedPhoneNumber()).forEach(System.out::println);
        Arrays.stream(
                numbers.removedPhoneNumber()).toList().forEach(number-> phoneBookRepository.
                        deleteByPhoneNumberAndUser(encrypt(number),user)
                );
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
        List<PhoneBook> phoneBooks = phoneBookRepository.findByUser(user);
        return getResponsePhoneBookDTO(user, phoneBooks);
    }


    public List<ResponseUserDTO> getUserInPhoneBookAndSetOff(CustomUserDetails userDetails,String phoneNumberInPhoneBook){
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotfoundException::new);
        List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(encrypt(phoneNumberInPhoneBook), user)
                .orElseThrow(PhoneBookNotfoundException::new);
        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(false));
        phoneBookRepository.saveAll(phoneBooks);
       return phoneBooks.stream().map(phoneBook -> ResponseUserDTO.builder().nickName(phoneBook.getNickName())
                .isCurtainCallOnAndOff(false)
                .build()).collect(Collectors.toList());

    }
    public ResponsePhoneBookDTO getPhoneBookWithSetAllOff(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotfoundException::new);
        List<PhoneBook> phoneBooks = phoneBookRepository.findByUser(user);

        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(false));
        phoneBookRepository.saveAll(phoneBooks);

        return getResponsePhoneBookDTO(user, phoneBooks);
    }
    public void setAllOnPhoneBook(CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getId()).orElseThrow(UserNotfoundException::new);
        List<PhoneBook> phoneBooks = phoneBookRepository.findByUser(user);
        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(true));
        phoneBookRepository.saveAll(phoneBooks);
    }


    private ResponsePhoneBookDTO getResponsePhoneBookDTO(User user, List<PhoneBook> phoneBooks) {
        Map<String, List<Contact>> contactMap = new HashMap<>();
        List<Contact> contacts = phoneBooks.stream()
                .map(phoneBook -> Contact.builder()
                        .phoneNumber(decrypt(phoneBook.getPhoneNumber()))
                        .name(phoneBook.getNickName())
                        .isCurtainCallOnAndOff(phoneBook.isCurtainCallOnAndOff())
                        .build())
                .collect(Collectors.toList());

        contactMap.put(decrypt(user.getPhoneNumber()), contacts);
        return ResponsePhoneBookDTO.builder().response(contactMap).build();
    }
    private String encrypt(String plainText) {
        return secretkeyManager.encrypt(plainText);
    }

    private String decrypt(String cipherText) {
        return secretkeyManager.decrypt(cipherText);
    }
}

package com.example.curtaincall.service;

import com.example.curtaincall.dto.request.RequestRemovedNumberInPhoneBookDTO;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.dto.response.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.response.ResponseUserDTO;
import com.example.curtaincall.global.auth.CurtaincallUserInfo;
import com.example.curtaincall.global.auth.jwt.JwtUtils;
import com.example.curtaincall.global.exception.PhoneBookNotfoundException;
import com.example.curtaincall.global.exception.UserNotfoundException;
import com.example.curtaincall.repository.PhoneBookRepository;
import com.example.curtaincall.repository.RecentCallLogRepository;
import com.example.curtaincall.repository.UserRepository;
import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.*;
import com.example.curtaincall.global.SecretkeyManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PhoneBookRepository phoneBookRepository;
    private final SecretkeyManager secretkeyManager;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, PhoneBookRepository phoneBookRepository, RecentCallLogRepository recentCallLogRepository, SecretkeyManager secretkeyManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.phoneBookRepository = phoneBookRepository;
        this.secretkeyManager = secretkeyManager;
        this.jwtUtils = jwtUtils;
    }

    public String saveUser(RequestUserDTO requestUserDTO) {
        if (userRepository.findByPhoneNumber(encrypt(requestUserDTO.phoneNumber())).isEmpty()) {
            log.info("save user info: {}", decrypt(encrypt(requestUserDTO.phoneNumber())));
            User user = userRepository.save(requestUserDTO.toEntity(encrypt(requestUserDTO.phoneNumber())));
            CurtaincallUserInfo userInfo = CurtaincallUserInfo.builder().id(user.getId())
                    .phoneNumber(user.getPhoneNumber())
                    .isCurtaincall(user.isCurtainCallOnAndOff())
                    .role(user.getUserRole())
                    .build();
            return jwtUtils.create(userInfo);
        }
        return "Not Save";
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

    public void updateUser(RequestUserDTO requestUserDTO, String prePhoneNumber) {
        User user = userRepository.findByPhoneNumber(prePhoneNumber).orElseThrow(
                UserNotfoundException::new
        );
        user.setNickName(requestUserDTO.nickName());
        user.setCurtainCallOnAndOff(requestUserDTO.isCurtainCall());
        if(!requestUserDTO.phoneNumber().equals(prePhoneNumber)) {
            user.setPhoneNumber(encrypt(requestUserDTO.phoneNumber()));
        }
        userRepository.save(user);
    }

    public void updatePhoneBook(Map<String, Contact> putRequestDTO, String userPhoneNumber) {

            User user = userRepository.findByPhoneNumber(userPhoneNumber).orElseThrow(
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
    public void deleteContaceInPhoneNumber(String phoneNumber,
                                           RequestRemovedNumberInPhoneBookDTO numbers){
        User user = userRepository.findByPhoneNumber(encrypt(phoneNumber)).orElseThrow(UserNotfoundException::new);

        Arrays.stream(numbers.removedPhoneNumber()).forEach(number->
                System.out.println(number));
        Arrays.stream(
                numbers.removedPhoneNumber()).toList().forEach(number-> phoneBookRepository.
                        deleteByPhoneNumberAndUser(encrypt(number),user)
                );
    }

    public ResponseUserDTO findUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                UserNotfoundException::new
        );
        return ResponseUserDTO.builder()
                .nickName(user.getNickName())
                .isCurtainCallOnAndOff(user.isCurtainCallOnAndOff())
                .build();
    }

    public ResponsePhoneBookDTO findPhoneBookByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                UserNotfoundException::new
        );
        List<PhoneBook> phoneBooks = phoneBookRepository.findByUser(user);
        return getResponsePhoneBookDTO(user, phoneBooks);
    }

    public List<Contact> getCurrentUserInfo(String userPhoneNumber, String postPhoneNumber) {
        User user = userRepository.findByPhoneNumber(encrypt(userPhoneNumber)).orElseThrow(
                UserNotfoundException::new
        );

        List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(encrypt(postPhoneNumber), user)
                .orElseThrow(PhoneBookNotfoundException::new);

        return phoneBooks.stream()
                .map(phoneBook -> Contact.builder()
                        .phoneNumber(decrypt(phoneBook.getPhoneNumber()))
                        .name(phoneBook.getNickName())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ResponseUserDTO> getUserInPhoneBookAndSetOff(String userPhoneNumber,String phoneNumberInPhoneBook){
        User user = userRepository.findByPhoneNumber(encrypt(userPhoneNumber)).orElseThrow(UserNotfoundException::new);
        List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(encrypt(phoneNumberInPhoneBook), user)
                .orElseThrow(PhoneBookNotfoundException::new);
        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(false));
        phoneBookRepository.saveAll(phoneBooks);
       return phoneBooks.stream().map(phoneBook -> ResponseUserDTO.builder().nickName(phoneBook.getNickName())
                .isCurtainCallOnAndOff(false)
                .build()).collect(Collectors.toList());

    }
    public ResponsePhoneBookDTO getPhoneBookWithRollback(String userPhoneNumber) {
        User user = userRepository.findByPhoneNumber(encrypt(userPhoneNumber)).orElseThrow(UserNotfoundException::new);
        List<PhoneBook> phoneBooks = phoneBookRepository.findByUser(user);

        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(false));
        phoneBookRepository.saveAll(phoneBooks);

        return getResponsePhoneBookDTO(user, phoneBooks);
    }
    public void setAllOnPhoneBook(String userPhoneNumber) {
        User user = userRepository.findByPhoneNumber(encrypt(userPhoneNumber)).orElseThrow(UserNotfoundException::new);
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

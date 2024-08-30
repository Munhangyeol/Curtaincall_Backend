package com.example.curtaincall.service;

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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PhoneBookRepository phoneBookRepository;
    private final SecretkeyManager secretkeyManager;

    public UserService(UserRepository userRepository, PhoneBookRepository phoneBookRepository, RecentCallLogRepository recentCallLogRepository, SecretkeyManager secretkeyManager) {
        this.userRepository = userRepository;
        this.phoneBookRepository = phoneBookRepository;
        this.secretkeyManager = secretkeyManager;
    }
    public void saveUser(RequestUserDTO requestUserDTO) {
        if(userRepository.findByPhoneNumber(requestUserDTO.phoneNumber()).isEmpty()) {
            userRepository.save(requestUserDTO.toEntity(secretkeyManager.encrypt(requestUserDTO.phoneNumber())));
        }}
    public void saveUserPhoneBooks(Map<String, List<Contact>>
                                           requestPhoneBookDTO){
//        System.out.println(r);

        for (String stringListEntry : requestPhoneBookDTO.keySet()) {
            User user = userRepository.findByPhoneNumber(secretkeyManager.encrypt(stringListEntry)).orElseThrow(
                    PhoneBookNotfoundException::new);
            requestPhoneBookDTO.get(stringListEntry).forEach(
                    contact -> phoneBookRepository.save(contact.toEntity(user,
                            secretkeyManager.encrypt(contact.getPhoneNumber())
                    ))//이거 그냥 saveall로 처리하는 방향으로 리펙토링하는걸로
            );
            requestPhoneBookDTO.get(stringListEntry).forEach(contact -> System.out.println("onandoff"+contact.getIsCurtainCallOnAndOff()));
        }
    }
    public void updateUser(RequestUserDTO requestUserDTO,String prePhoneNumber){
        User user = userRepository.findByPhoneNumber(secretkeyManager.encrypt(prePhoneNumber)).orElseThrow(
                UserNotfoundException::new
        );
            user.setNickName(requestUserDTO.nickName());
            if(!requestUserDTO.phoneNumber().equals(prePhoneNumber))
                user.setPhoneNumber(secretkeyManager.encrypt(requestUserDTO.phoneNumber()));
            userRepository.save(user);
    }
    public void updatePhoneBook(Map<String, Contact> putRequestDTO, String prePhoneNumber) {
        for (String stringListEntry : putRequestDTO.keySet()) {
            User user = userRepository.findByPhoneNumber(secretkeyManager.encrypt(stringListEntry)).orElseThrow(
                    UserNotfoundException::new);
            List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(secretkeyManager.encrypt(prePhoneNumber), user).
                    orElseThrow(PhoneBookNotfoundException::new);
            if(phoneBooks.isEmpty()) {
                throw new PhoneBookNotfoundException();
            }
            for (PhoneBook phoneBook : phoneBooks) {
                phoneBook.setPhoneNumber(secretkeyManager.encrypt(putRequestDTO.get(stringListEntry).getPhoneNumber()));
                phoneBook.setNickName(putRequestDTO.get(stringListEntry).getName());
                phoneBook.setCurtainCallOnAndOff(putRequestDTO.get(stringListEntry).getIsCurtainCallOnAndOff());
            }
            phoneBookRepository.saveAll(phoneBooks);
        }
    }
    public ResponseUserDTO findUserByPhoneNumber(String phoneNumber){
        User user = userRepository.findByPhoneNumber(secretkeyManager.encrypt(phoneNumber)).orElseThrow(
                UserNotfoundException::new
                //exception을 반환하기 보다는,exceptionhandler를 이용해서 프론트 단으로 user가 현재 등록이 안되어 있음을
                //알려야함
        );
        return ResponseUserDTO.builder().nickName(user.getNickName())
                .isCurtainCallOnAndOff(true)
                .build();
    }
    public ResponsePhoneBookDTO findPhoneBookByPhoneNumber(String phoneNumber){
        User user=userRepository.findByPhoneNumber(secretkeyManager.encrypt(phoneNumber)).orElseThrow(
                UserNotfoundException::new
        );
        List<PhoneBook> phoneBooks= phoneBookRepository.findByUser(user);
        Map<String, List<Contact>> contactMap = new HashMap<>();
        List<Contact> contacts = new ArrayList<>();
        phoneBooks.forEach(
                phoneBook -> contacts.add(Contact.builder()
                        .phoneNumber(secretkeyManager.decrypt(phoneBook.getPhoneNumber()))
                        .name(phoneBook.getNickName())
                                .isCurtainCallOnAndOff(phoneBook.isCurtainCallOnAndOff())
                        .build())
        );
        contacts.forEach(contact -> System.out.println("!!"+contact.getIsCurtainCallOnAndOff()));
//        phoneBooks.forEach(phoneBook -> System.out.println(phoneBook.isCurtainCallOnAndOff()));
        contactMap.put(secretkeyManager.decrypt(user.getPhoneNumber()), contacts);
        contactMap.get("01023326094").forEach(contact -> System.out.println(contact.getIsCurtainCallOnAndOff()));
        return ResponsePhoneBookDTO.builder().response(contactMap).build();
    }
    public List<Contact> getCurrentUserInfo(String userPhoneNumber,String postPhoneNumber){
        User user = userRepository.findByPhoneNumber(secretkeyManager.encrypt(userPhoneNumber)).orElseThrow(
                UserNotfoundException::new
        );
        List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(secretkeyManager.encrypt(postPhoneNumber), user)
                .orElseThrow(PhoneBookNotfoundException::new);
        if(phoneBooks.isEmpty()){
            throw new PhoneBookNotfoundException();
        }
        return phoneBooks.stream().map(phoneBook -> Contact.builder().
                phoneNumber(secretkeyManager.decrypt(phoneBook.getPhoneNumber())).name(phoneBook.getNickName()
                        ).build()).collect(Collectors.toList());
    }


}

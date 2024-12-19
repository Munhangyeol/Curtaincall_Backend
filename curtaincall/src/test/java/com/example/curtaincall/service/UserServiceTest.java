package com.example.curtaincall.service;

import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestRemovedNumberInPhoneBookDTO;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.dto.response.ResponsePhoneBookDTO;

import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.auth.CurtaincallUserInfo;
import com.example.curtaincall.global.exception.UserAlreadyExistsException;
import com.example.curtaincall.global.exception.UserNotfoundException;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import com.example.curtaincall.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    private CustomUserDetails userDetails1;
    private CustomUserDetails userDetails2;
    private CustomUserDetails userDetails3;
    @PostConstruct
    public void postConstruct(){
        User user1 = userRepository.findByPhoneNumber("01023326094")
                .orElseThrow(UserNotfoundException::new);
        User user2 = userRepository.findByPhoneNumber("01012345678")
                .orElseThrow(UserNotfoundException::new);
        userDetails1= new CustomUserDetails(CurtaincallUserInfo.builder()
                .id(user1.getId())
                .phoneNumber(user1.getPhoneNumber())
                .isCurtaincall(user1.isCurtainCallOnAndOff())
                .role(user1.getUserRole())
                .nickName(user1.getNickName())
                .build()
        );
        userDetails2= new CustomUserDetails(CurtaincallUserInfo.builder()
                .id(user2.getId())
                .phoneNumber(user2.getPhoneNumber())
                .isCurtaincall(user2.isCurtainCallOnAndOff())
                .role(user2.getUserRole())
                .nickName(user2.getNickName())
                .build()
        );
    }


    @DisplayName("user테이블에 있는 번호로 찾으면 user가 잘 반환됨")
    @Test
    public void findByPhoneNumberInUserTable(){
        Assertions.assertNotNull(userService.findUser(userDetails1));
    }
    @DisplayName("PhoneBook테이블에 있는 번호로 찾으면 잘 반환됨.")
    @Test
    public void findByPhoneNumberInPhoneBookTable(){
        Assertions.assertNotNull(userService.findPhoneBook(userDetails1));
    }
    @DisplayName("user테이블에 이미 있는 번호를 저장 하면 예외가 발생한다.")
    @Test
    public void saveByPhoneNumberInUserTable(){Assertions.assertThrows(UserAlreadyExistsException.class,()->userService.saveUser(RequestUserDTO.builder()
                .phoneNumber("01023326094").nickName("nickname").isCurtainCall(true).build()));
    }
    @DisplayName("user테이블에 없는 번호를 저장 하면 잘 동작한다.")
    @Test
    public void saveByPhoneNumberUserTable(){
        Assertions.assertNotNull(UserAlreadyExistsException.class,()->userService.saveUser(RequestUserDTO.builder()
            .phoneNumber("01023326091").nickName("nickname").isCurtainCall(true).build()));
    }

    @Transactional
    @DisplayName("01023326094 user의 번호들을 일괄 on으로 전환")
    @Test
    public void setAllOnCurtaincall(){
        userService.setAllOnPhoneBook(userDetails1);
        ResponsePhoneBookDTO phoneBookByPhoneNumber = userService.findPhoneBook(userDetails1);
        phoneBookByPhoneNumber.getResponse().get("01023326094").forEach(phoneBook->
                Assertions.assertEquals(phoneBook.getIsCurtainCallOnAndOff(),true));
    }
    @Transactional
    @DisplayName("01023326094 user의 번호들을 일괄 off로 전환")
    @Test
    public void setAllOffCurtaincall(){
        ResponsePhoneBookDTO phoneBookByPhoneNumber = userService.getPhoneBookWithSetAllOff(userDetails1);
        phoneBookByPhoneNumber.getResponse().get("01023326094").forEach(phoneBook->
                Assertions.assertEquals(phoneBook.getIsCurtainCallOnAndOff(),false));
    }
    @Transactional
    @DisplayName("01023326094 user의 특정 번호들을  off로 전환")
    @Test
    public void setOffCurtaincall(){
     userService.getUserInPhoneBookAndSetOff(userDetails1,
                "01044444444");
        ResponsePhoneBookDTO phoneBookByPhoneNumber1 = userService.findPhoneBook(userDetails1);
        Optional<Contact> first = phoneBookByPhoneNumber1.getResponse().get("01023326094")
                .stream().filter(phoneBook -> Objects.equals(phoneBook.getPhoneNumber(), "01044444444"))
                .findFirst();
        Assertions.assertEquals(first.get().getIsCurtainCallOnAndOff(),false);
    }
    @Transactional
    @DisplayName("01023326094 user의 정보를 변경")
    @Test
    public void changeUser(){
     userService.updateUser(RequestUserDTO.builder().phoneNumber("01033333333")
             .nickName("MunMun")
             .isCurtainCall(true).build(),userDetails1);
     Assertions.assertEquals("MunMun",userService.findUser(userDetails1).nickName());
    }
    @Transactional
    @DisplayName("01023326094 phoneBook의 정보를 변경")
    @Test
    public void changePhoneBook(){
        Map<String,Contact> maps=new HashMap<>();
        maps.put("01033333333", Contact.builder()
                .phoneNumber("01044415552")
                .name("조조")
                .isCurtainCallOnAndOff(true)
                .build());
        userService.updatePhoneBook(maps,userDetails1);
        Optional<Contact> first = userService.findPhoneBook(userDetails1).getResponse().
                get("01023326094")
                .stream().filter(contact -> Objects.equals(contact.getPhoneNumber(),
                        "01044415552")).findFirst();
        Assertions.assertEquals(first.get().getName(),"조조");
    }
    @Transactional
    @DisplayName("01023326094 user의 contact 변경")
    @Test
    public void deleteContact(){
        String []str= new String[]{"01033333333"};
        RequestRemovedNumberInPhoneBookDTO numbers=RequestRemovedNumberInPhoneBookDTO.builder()
                        .removedPhoneNumber(str).build();
        userService.deleteContactInPhoneNumber(userDetails1,numbers);
        Assertions.assertEquals(1,userService.findPhoneBook(userDetails1).getResponse().get("01023326094").size());
    }





}

package com.example.curtaincall.service;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.dto.response.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.response.ResponseUserDTO;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


//TODO user Update, PhoneBook Update, user delete 이 세개의 테스트 코드만 추가하면 됨
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private SecretkeyManager secretkeyManager;

    @DisplayName("user테이블에 없는 번호로 찾으면 예외가 발생한다.")
    @Test
    public void findByPhoneNumberNotInUserTable(){

        Assertions.assertThrows(RuntimeException.class, () -> userService.findUserByPhoneNumber(secretkeyManager.encrypt("01023326093")));
    }
    @DisplayName("user테이블에 있는 번호로 찾으면 user가 잘 반환됨")
    @Test
    public void findByPhoneNumberInUserTable(){
        Assertions.assertNotNull(userService.findUserByPhoneNumber(secretkeyManager.encrypt("01023326094")));
    }
    @DisplayName("PhoneBook테이블에 있는 번호로 찾으면 잘 반환됨.")
    @Test
    public void findByPhoneNumberInPhoneBookTable(){
        Assertions.assertNotNull(userService.findPhoneBookByPhoneNumber(secretkeyManager.encrypt("01023326094")));
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
        userService.setAllOnPhoneBook(secretkeyManager.encrypt("01023326094"));
        ResponsePhoneBookDTO phoneBookByPhoneNumber = userService.findPhoneBookByPhoneNumber(secretkeyManager.encrypt("01023326094"));
        phoneBookByPhoneNumber.getResponse().get("01023326094").forEach(phoneBook->
                Assertions.assertEquals(phoneBook.getIsCurtainCallOnAndOff(),true));
    }
    @Transactional
    @DisplayName("01023326094 user의 번호들을 일괄 off로 전환")
    @Test
    public void setAllOffCurtaincall(){
        ResponsePhoneBookDTO phoneBookByPhoneNumber = userService.getPhoneBookWithSetAllOff(secretkeyManager.encrypt("01023326094"));
        phoneBookByPhoneNumber.getResponse().get("01023326094").forEach(phoneBook->
                Assertions.assertEquals(phoneBook.getIsCurtainCallOnAndOff(),false));
    }
    @Transactional
    @DisplayName("01023326094 user의 특정 번호들을  off로 전환")
    @Test
    public void setOffCurtaincall(){
     userService.getUserInPhoneBookAndSetOff(secretkeyManager.encrypt("01023326094"),
                "01044444444");
        ResponsePhoneBookDTO phoneBookByPhoneNumber1 = userService.findPhoneBookByPhoneNumber(secretkeyManager.encrypt("01023326094"));
        Optional<Contact> first = phoneBookByPhoneNumber1.getResponse().get("01023326094")
                .stream().filter(phoneBook -> Objects.equals(phoneBook.getPhoneNumber(), "01044444444"))
                .findFirst();
        Assertions.assertEquals(first.get().getIsCurtainCallOnAndOff(),false);
    }




}

package com.example.curtaincall.service;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.RequestUserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@SpringBootTest
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @DisplayName("user테이블에 phoneNumber와 이름이 정상적으로 저장하고 조회되는지를 확인한다.")
    @Test
    public void createUser(){
        createUserAndPhoneBook();
        //then
        Assertions.assertNotNull(userService.findUserByPhoneNumber("01023326094"));
    }
    @DisplayName("user테이블에 없는 번호로 찾으면 예외가 발생한다.")
    @Test
    public void findByPhoneNumberNotInUserTable(){
        createUserAndPhoneBook();
        //then
        Assertions.assertThrows(RuntimeException.class,()->userService.findUserByPhoneNumber("01023326093"));
    }

    @DisplayName("phoneBook테이블에 전화번호 정보들이 잘 저장 되었는지 확인.")
    @Test
    public void createPhoneBook(){
        createUserAndPhoneBook();
        //then
        Assertions.assertNotNull(userService.findPhoneBookByPhoneNumber("01023326094"));
    }



    @DisplayName("user의 이름이 없데이트가 잘 되는지를 확인.")
    @Test
    public void updateUserWhenUpdateName(){
        //given
        RequestUserDTO requestUserDTO = RequestUserDTO.builder().phoneNumber("01023326094")
                .nickName("문한결").build();
        userService.saveUser(requestUserDTO);
        RequestUserDTO requestNewUserDTO = RequestUserDTO.builder().phoneNumber("01023326094")
                .nickName("문한별").build();
        userService.updateUser(requestNewUserDTO, "01023326094");
        Assertions.assertEquals("문한별", userService.findUserByPhoneNumber("01023326094").nickName());
    }
    @DisplayName("user의 번호가 업데이트가 잘 되는지를 확인.")
    @Test
    public void updateUserWhenUpdatePhoneNumber(){
        //given
        RequestUserDTO requestUserDTO = RequestUserDTO.builder().phoneNumber("01023326094")
                .nickName("문한결").build();
        userService.saveUser(requestUserDTO);
        RequestUserDTO requestNewUserDTO = RequestUserDTO.builder().phoneNumber("01033326094")
                .nickName("문한결").build();
        userService.updateUser(requestNewUserDTO, "01023326094");
        Assertions.assertNotNull(userService.findUserByPhoneNumber("01033326094"));
    }

    private void createUserAndPhoneBook() {
        //given
        RequestUserDTO requestUserDTO = RequestUserDTO.builder().phoneNumber("01023326094")
                .nickName("문한결").build();
        userService.saveUser(requestUserDTO);
        Map<String, List<Contact>> maps = new HashMap<>();
        Contact contact1 = new Contact("조한흠", "01012345678",false);
        Contact contact2 = new Contact("박성준", "01098765678",false);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        maps.put("01023326094", contacts);
        //when
        userService.saveUserPhoneBooks(maps);
    }


}

package com.example.curtaincall.service;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.global.SecretkeyManager;
import org.junit.jupiter.api.Assertions;
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
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @DisplayName("user테이블에 없는 번호로 찾으면 예외가 발생한다.")
    @Test
    public void findByPhoneNumberNotInUserTable(){

        Assertions.assertThrows(RuntimeException.class,()->userService.findUserByPhoneNumber("01023326093"));
    }
    @DisplayName("user테이블에 있는 번호로 찾으면 잘 동작한다.")
    @Test
    public void findByPhoneNumberInUserTable(){
        Assertions.assertNotNull(userService.findUserByPhoneNumber("01023326094"));
    }




}

package com.example.curtaincall.service;

import com.example.curtaincall.dto.RequestUserDTO;
import com.example.curtaincall.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthorizationTest {
    @Autowired
    private AuthorizaionService authorizaionService;

    @Autowired
    private  UserService userService;

    @DisplayName("맨 처음에 user인지 아닌지를 반환")
    @Test
    public void isUser(){
        //given
        RequestUserDTO requestUserDTO = RequestUserDTO.builder().phoneNumber("01023326094")
                .nickName("문한결").build();
        userService.saveUser(requestUserDTO);
        //when
        Assertions.assertEquals(true, authorizaionService.isUser(requestUserDTO.phoneNumber()));
    }
}

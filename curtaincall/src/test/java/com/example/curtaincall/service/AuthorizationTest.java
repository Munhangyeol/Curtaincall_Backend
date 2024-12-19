package com.example.curtaincall.service;

import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.auth.CurtaincallUserInfo;
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

@SpringBootTest
@ActiveProfiles("test")
public class AuthorizationTest {
    @Autowired
    private AuthorizaionService authorizaionService;

    @Autowired
    private UserRepository userRepository;
    private CustomUserDetails userDetails1;
    @PostConstruct
    public void postConstruct() {
        User user1 = userRepository.findByPhoneNumber("01023326094")
                .orElseThrow(UserNotfoundException::new);

        userDetails1 = new CustomUserDetails(CurtaincallUserInfo.builder()
                .id(user1.getId())
                .phoneNumber(user1.getPhoneNumber())
                .isCurtaincall(user1.isCurtainCallOnAndOff())
                .role(user1.getUserRole())
                .nickName(user1.getNickName())
                .build()
        );
    }

    @DisplayName("맨 처음에 user인지 아닌지를 반환")
    @Test
    public void isUser(){
        //given
        RequestUserDTO requestUserDTO = RequestUserDTO.builder().phoneNumber("01023326094")
                .nickName("문한결").build();
        //when
        Assertions.assertEquals(true, authorizaionService.isUser(userDetails1));
    }
}

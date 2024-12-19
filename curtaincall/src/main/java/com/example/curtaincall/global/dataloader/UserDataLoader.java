package com.example.curtaincall.global.dataloader;

import com.example.curtaincall.domain.User;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader {
    private final UserRepository userRepository;



    public UserDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public void saveUser(){
        userRepository.save(User.builder().nickName("문한결")
                .phoneNumber("01023326094")
                .isCurtainCallOnAndOff(true).build());
        userRepository.save(User.builder().nickName("조한흠")
                .phoneNumber("01012345678")
                .isCurtainCallOnAndOff(false).build());

    }
}

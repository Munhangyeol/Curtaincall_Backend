package com.example.curtaincall.dto;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Contact {
    private final String name;
    @Getter
    private final String phoneNumber;
    private final Boolean isCurtainCallOnAndOff;



    // Getters and Setters

    @Builder
    public Contact(String name, String phoneNumber, Boolean isCurtainCallOnAndOff){
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.isCurtainCallOnAndOff = isCurtainCallOnAndOff;
    }
    public PhoneBook toEntity(User user,String encryptNumber){
        return PhoneBook.builder()
                .phoneNumber(encryptNumber).nickName(name)
                .isCurtainCallOnAndOff(isCurtainCallOnAndOff)
                .user(user).build();
    }
}

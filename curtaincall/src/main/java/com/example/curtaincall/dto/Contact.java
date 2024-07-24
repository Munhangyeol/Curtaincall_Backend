package com.example.curtaincall.dto;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import lombok.Builder;

public class Contact {
    private final String name;
    private final String phoneNumber;

    public String getName() {
        return this.name;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    // Getters and Setters

    @Builder
    public Contact(String name,String phoneNumber){
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    public PhoneBook toEntity(User user){
        return PhoneBook.builder()
                .phoneNumber(phoneNumber).nickName(name)
                .user(user).build();
    }
}

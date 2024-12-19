package com.example.curtaincall.domain;

import com.example.curtaincall.global.PhoneNumberConverter;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
//@RequiredArgsConstructor
public class PhoneBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickName;
    @Convert(converter = PhoneNumberConverter.class)
    private String phoneNumber;
    private boolean isCurtainCallOnAndOff;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PhoneBook(){

    }
    @Builder
    public PhoneBook(String nickName,String phoneNumber,User user,Boolean isCurtainCallOnAndOff){
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.user = user;
        this.isCurtainCallOnAndOff = isCurtainCallOnAndOff;
    }

    
}

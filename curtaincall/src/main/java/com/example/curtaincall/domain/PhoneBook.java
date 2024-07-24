package com.example.curtaincall.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
//@RequiredArgsConstructor
public class PhoneBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickName;
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public PhoneBook(){

    }
    @Builder
    public PhoneBook(String nickName,String phoneNumber,User user){
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.user = user;
    }

    
}

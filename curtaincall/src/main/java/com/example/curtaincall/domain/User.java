package com.example.curtaincall.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@Entity
@Getter
@Setter
//@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "user_id")
    private Long id;
    private String phoneNumber;
    private String nickName;
    private boolean isCurtainCallOnAndOff;

//    @Builder
    public User() {

    }
    @Builder
    public User(String phoneNumber,String nickName,boolean isCurtainCallOnAndOff){
        this.phoneNumber=phoneNumber;
        this.nickName = nickName;
        this.isCurtainCallOnAndOff=isCurtainCallOnAndOff;
    }




}

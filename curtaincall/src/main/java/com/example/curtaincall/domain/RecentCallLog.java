package com.example.curtaincall.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class RecentCallLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isMissedCall;
    private String nickName;
    private String phoneNumber;

    private Date recentCallDate;

    @Builder
    public RecentCallLog(boolean isMissedCall,String nickName,
    String phoneNumber
    , Date recentCallDate){
        this.isMissedCall = isMissedCall;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.recentCallDate = recentCallDate;
    }

    public RecentCallLog() {

    }
}

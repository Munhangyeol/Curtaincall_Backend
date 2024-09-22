package com.example.curtaincall.global.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurtaincallUserInfo {
    private Long id;
    private String phoneNumber;
    private String nickName;
    private boolean isCurtaincall;
    private String role;
}

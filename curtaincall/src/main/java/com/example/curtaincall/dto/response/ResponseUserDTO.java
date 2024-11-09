package com.example.curtaincall.dto.response;

import lombok.Builder;

public record ResponseUserDTO(String phoneNumber,String nickName, boolean isCurtainCallOnAndOff) {

    @Builder
    public ResponseUserDTO(String phoneNumber,String nickName,boolean isCurtainCallOnAndOff){
        this.phoneNumber=phoneNumber;
        this.nickName = nickName;
        this.isCurtainCallOnAndOff = isCurtainCallOnAndOff;
    }
}

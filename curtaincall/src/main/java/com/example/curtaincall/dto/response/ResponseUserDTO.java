package com.example.curtaincall.dto.response;

import lombok.Builder;

public record ResponseUserDTO(String nickName, boolean isCurtainCallOnAndOff) {

    @Builder
    public ResponseUserDTO(String nickName,boolean isCurtainCallOnAndOff){
        this.nickName = nickName;
        this.isCurtainCallOnAndOff = isCurtainCallOnAndOff;
    }
}

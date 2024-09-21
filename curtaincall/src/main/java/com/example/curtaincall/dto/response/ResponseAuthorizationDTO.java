package com.example.curtaincall.dto.response;

import lombok.Builder;

public record ResponseAuthorizationDTO(String message, Boolean isUser) {
    @Builder
    public ResponseAuthorizationDTO(String message, Boolean isUser){
        this.message = message;
        this.isUser = isUser;
    }
}

package com.example.curtaincall.dto.request;

import lombok.Builder;

public record RequestRemovedNumberInPhoneBookDTO(String[] removedPhoneNumber) {
    @Builder
    public RequestRemovedNumberInPhoneBookDTO(String[] removedPhoneNumber){
        this.removedPhoneNumber=removedPhoneNumber;
    }
}

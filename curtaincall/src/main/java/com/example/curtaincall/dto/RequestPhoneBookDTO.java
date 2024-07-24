package com.example.curtaincall.dto;

import lombok.Builder;

import java.util.List;
import java.util.Map;

public record RequestPhoneBookDTO(Map<String, List<Contact>> request) {

    @Builder
    public RequestPhoneBookDTO(Map<String,List<Contact>> request){
        this.request = request;
    }
}



package com.example.curtaincall.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class ResponsePhoneBookDTO {
    Map<String, List<Contact>> request;
    @Builder
    public ResponsePhoneBookDTO(Map<String,List<Contact>> request){
        this.request = request;
    }
}

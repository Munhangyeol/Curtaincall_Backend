package com.example.curtaincall.dto.response;

import com.example.curtaincall.dto.Contact;
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
    Map<String, List<Contact>> response;
    @Builder
    public ResponsePhoneBookDTO(Map<String,List<Contact>> response){
        this.response = response;
    }
}

package com.example.curtaincall.global;

import jakarta.persistence.AttributeConverter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PhoneNumberConverter implements AttributeConverter<String,String> {
    private final SecretkeyManager secretkeyManager;
    @Override
    public String convertToDatabaseColumn(String s) {
        return secretkeyManager.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return secretkeyManager.decrypt(s);
    }
}

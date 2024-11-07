package com.example.curtaincall.domain;

import jakarta.persistence.PostUpdate;

public class UserListener {
    @PostUpdate
    public void onPostUpdate(User user) {
        // 관련된 PhoneBook 엔티티들을 업데이트
        for (PhoneBook phoneBook : user.getPhoneBooks()) {
            phoneBook.setUser(user);
        }
    }
}
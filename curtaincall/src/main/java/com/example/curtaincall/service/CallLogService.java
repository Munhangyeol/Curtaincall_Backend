package com.example.curtaincall.service;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.response.ResponseRecentCallLogDTO.CallLogInfo;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.exception.PhoneBookNotfoundException;
import com.example.curtaincall.global.exception.UserNotfoundException;
import com.example.curtaincall.repository.PhoneBookRepository;
import com.example.curtaincall.dto.request.RequestRecentCallLogDTO;
import com.example.curtaincall.dto.response.ResponseRecentCallLogDTO;
import com.example.curtaincall.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CallLogService {
    private final UserRepository userRepository;
    private final PhoneBookRepository phoneBookRepository;
    private final SecretkeyManager secretkeyManager;
    public CallLogService(UserRepository userRepository, PhoneBookRepository phoneBookRepository, SecretkeyManager secretkeyManager) {
        this.userRepository = userRepository;
        this.phoneBookRepository = phoneBookRepository;
        this.secretkeyManager = secretkeyManager;
    }
    public ResponseRecentCallLogDTO findRecentCallContact(String userPhoneNumber,RequestRecentCallLogDTO recentPhoneNumbers){
        User user = userRepository.findByPhoneNumber(userPhoneNumber).orElseThrow(
                UserNotfoundException::new);
        List<CallLogInfo> callLogInfos = new ArrayList<>();
        for (String phoneNumber : recentPhoneNumbers.phoneNumbers()) {

            List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(
                    secretkeyManager.encrypt(phoneNumber), user)
                    .orElseThrow(PhoneBookNotfoundException::new);
            if(phoneBooks.isEmpty())
                throw new PhoneBookNotfoundException();
//            System.out.println("!!!!!"+phoneBooks);

            for (PhoneBook phoneBook : phoneBooks) {
                callLogInfos.add(CallLogInfo.builder().
                        nickname(phoneBook.getNickName()).
                        phoneNumber(secretkeyManager.decrypt(phoneBook.getPhoneNumber())).build());
            }
        }
        return ResponseRecentCallLogDTO.builder().callLogInfos(callLogInfos).build();
    }
}

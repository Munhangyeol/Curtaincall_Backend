package com.example.curtaincall.service;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.ResponseRecentCallLogDTO.CallLogInfo;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.exception.PhoneBookNotfoundException;
import com.example.curtaincall.global.exception.UserNotfoundException;
import com.example.curtaincall.repository.PhoneBookRepository;
import com.example.curtaincall.repository.RecentCallLogRepository;
import com.example.curtaincall.domain.RecentCallLog;
import com.example.curtaincall.dto.RequestRecentCallLogDTO;
import com.example.curtaincall.dto.ResponseRecentCallLogDTO;
import com.example.curtaincall.repository.UserRepository;
import okhttp3.Call;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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
        User user = userRepository.findByPhoneNumber(secretkeyManager.encrypt(userPhoneNumber)).orElseThrow(
                UserNotfoundException::new);
        List<CallLogInfo> callLogInfos = new ArrayList<>();
        for (String phoneNumber : recentPhoneNumbers.phoneNumbers()) {
            List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(
                    secretkeyManager.encrypt(phoneNumber), user)
                    .orElseThrow(PhoneBookNotfoundException::new);
            if(phoneBooks.isEmpty())
            {
                callLogInfos.add(CallLogInfo.builder().
                        nickname("").
                        phoneNumber(phoneNumber).build());
            }
            for (PhoneBook phoneBook : phoneBooks) {
                callLogInfos.add(CallLogInfo.builder().
                        nickname(phoneBook.getNickName()).
                        phoneNumber(secretkeyManager.decrypt(phoneBook.getPhoneNumber())).build());
            }
        }
        return ResponseRecentCallLogDTO.builder().callLogInfos(callLogInfos).build();
    }
}

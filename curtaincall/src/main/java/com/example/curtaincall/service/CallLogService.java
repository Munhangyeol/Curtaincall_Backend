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
import lombok.RequiredArgsConstructor;
import okhttp3.Call;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CallLogService {
    private final UserRepository userRepository;
    private final PhoneBookRepository phoneBookRepository;

    public ResponseRecentCallLogDTO findRecentCallContact(String userPhoneNumber,RequestRecentCallLogDTO recentPhoneNumbers){
        System.out.println(userPhoneNumber);
        User user = userRepository.findByPhoneNumber(userPhoneNumber).orElseThrow(
                UserNotfoundException::new);
        List<CallLogInfo> callLogInfos = new ArrayList<>();
        findRecentCallDTO(recentPhoneNumbers, user, callLogInfos);
        return ResponseRecentCallLogDTO.builder().callLogInfos(callLogInfos).build();
    }

    private void findRecentCallDTO(RequestRecentCallLogDTO recentPhoneNumbers, User user, List<CallLogInfo> callLogInfos) {
        for (String phoneNumber : recentPhoneNumbers.phoneNumbers()) {
            List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(
                    phoneNumber, user)
                    .orElseThrow(PhoneBookNotfoundException::new);
            if(phoneBooks.isEmpty())
                setCallLogInfosNotExistName(callLogInfos, phoneNumber);
            setCallLogInfosExistName(callLogInfos, phoneBooks);
        }
    }

    private void setCallLogInfosExistName(List<CallLogInfo> callLogInfos, List<PhoneBook> phoneBooks) {
        for (PhoneBook phoneBook : phoneBooks) {
            callLogInfos.add(CallLogInfo.builder().
                    nickname(phoneBook.getNickName()).
                    phoneNumber(phoneBook.getPhoneNumber()).build());
        }
    }

    private void setCallLogInfosNotExistName(List<CallLogInfo> callLogInfos, String phoneNumber) {
        callLogInfos.add(CallLogInfo.builder().phoneNumber(phoneNumber)
                .nickname("Unknown!!")
                .build());
    }
}

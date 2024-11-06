package com.example.curtaincall.service;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestRecentCallLogDTO;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.exception.PhoneBookNotfoundException;
import com.example.curtaincall.repository.PhoneBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.curtaincall.dto.response.ResponseRecentCallLogDTO.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CallLogServiceTest {
    @Autowired
    private CallLogService callLogService;

    @Autowired
    private SecretkeyManager manager;

    @DisplayName("두개 이상의 번호를 통해서 request 순서대로, response를 잘 반환하는지 확인")
    @Test
    public void findRecentCallByDuplicateNumber(){
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("01033333333");
        phoneNumbers.add("01044444444");
        RequestRecentCallLogDTO recentCallLogDTO = new RequestRecentCallLogDTO(phoneNumbers);
        List<CallLogInfo> callLogInfos = new ArrayList<>();

        callLogInfos.add(CallLogInfo.builder().nickname("김김김")
                .phoneNumber("01033333333").build());
        callLogInfos.add(CallLogInfo.builder().nickname("임임임")
                .phoneNumber("01044444444").build());
        Assertions.assertEquals(callLogService.findRecentCallContact(manager.encrypt("01023326094"),recentCallLogDTO).callLogInfos(),
                callLogInfos);
    }
    @DisplayName("없는 번호를 찾으면 exception을 반환 ")
    @Test
    public void findRecentCallByNoNumber(){
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("01032345678");
        phoneNumbers.add("01048765678");

        RequestRecentCallLogDTO recentCallLogDTO = new RequestRecentCallLogDTO(phoneNumbers);

        Assertions.assertThrows(PhoneBookNotfoundException.class, () -> callLogService.findRecentCallContact(manager.encrypt("01023326094"),
                recentCallLogDTO)
        );
    }

}

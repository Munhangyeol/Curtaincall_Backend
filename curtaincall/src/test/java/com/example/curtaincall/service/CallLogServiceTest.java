package com.example.curtaincall.service;

import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestRecentCallLogDTO;
import com.example.curtaincall.dto.request.RequestUserDTO;
import com.example.curtaincall.global.exception.PhoneBookNotfoundException;
import com.example.curtaincall.repository.PhoneBookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.curtaincall.dto.response.ResponseRecentCallLogDTO.*;

@SpringBootTest
@Transactional
public class CallLogServiceTest {
    @Autowired
    private CallLogService callLogService;
    @Autowired
    private UserService userService;
    @Autowired
    private PhoneBookRepository phoneBookRepository;

    @DisplayName("두개 이상의 번호를 통해서 request 순서대로, response를 잘 반환하는지 확인")
    @Test
    public void findRecentCallByDuplicateNumber(){
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("01012345678");
        phoneNumbers.add("01098765678");
        phoneNumbers.add("01012345678");
        RequestRecentCallLogDTO recentCallLogDTO = new RequestRecentCallLogDTO(phoneNumbers);
        initialBeforeFindRecentCall();
        List<CallLogInfo> callLogInfos = new ArrayList<>();
        callLogInfos.add(CallLogInfo.builder().nickname("조한흠")
                .phoneNumber("01012345678").build());
        callLogInfos.add(CallLogInfo.builder().nickname("박성준")
                .phoneNumber("01098765678").build());
        callLogInfos.add(CallLogInfo.builder().nickname("조한흠")
                .phoneNumber("01012345678").build());
        Assertions.assertEquals(callLogService.findRecentCallContact("01023326094",recentCallLogDTO).callLogInfos(),
                callLogInfos);
    }
    @DisplayName("없는 번호를 찾으면 exception을 반환 ")
    @Test
    public void findRecentCallByNoNumber(){
        List<String> phoneNumbers = new ArrayList<>();
        phoneNumbers.add("01032345678");
        phoneNumbers.add("01048765678");
        phoneNumbers.add("01012345678");
        RequestRecentCallLogDTO recentCallLogDTO = new RequestRecentCallLogDTO(phoneNumbers);
        initialBeforeFindRecentCall();

        Assertions.assertThrows(PhoneBookNotfoundException.class, () -> callLogService.findRecentCallContact("01023326094", recentCallLogDTO)
        );
    }
    private void initialBeforeFindRecentCall() {

        RequestUserDTO requestUserDTO = RequestUserDTO.builder().phoneNumber("01023326094")
                .nickName("문한결").build();
        userService.saveUser(requestUserDTO);
        Map<String, List<Contact>> maps = new HashMap<>();
        Contact contact1 = new Contact("조한흠", "01012345678",false);
        Contact contact2 = new Contact("박성준", "01098765678",false);
        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        maps.put("01023326094", contacts);
        userService.saveUserPhoneBooks(maps);
    }
}

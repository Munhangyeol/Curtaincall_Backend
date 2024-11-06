package com.example.curtaincall.global.dataloader;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.repository.PhoneBookRepository;
import com.example.curtaincall.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PhoneBookDataLoader {
    private final PhoneBookRepository phoneBookRepository;
    private final UserRepository userRepository;
    private final SecretkeyManager manager;


    public void save(){
        User user1=userRepository.findByPhoneNumber(manager.encrypt("01023326094"))
                .orElseThrow(RuntimeException::new);
        User user2=userRepository.findByPhoneNumber(manager.encrypt("01012345678"))
                .orElseThrow(RuntimeException::new);
        Contact contact1=Contact.builder().phoneNumber(manager.encrypt("01033333333"))
                        .name("김김김")
                                .isCurtainCallOnAndOff(false).build();
        Contact contact2=Contact.builder().phoneNumber(manager.encrypt("01044444444"))
                .name("임임임")
                .isCurtainCallOnAndOff(true).build();
        List<Contact> contacts=new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        List<PhoneBook> phoneBooks1=contacts.stream().map(contact->
                PhoneBook.builder().user(user1)
                        .phoneNumber(contact.getPhoneNumber())
                        .isCurtainCallOnAndOff(contact.getIsCurtainCallOnAndOff())
                        .nickName(contact.getName())
                        .build()).toList();
        List<PhoneBook> phoneBooks2=contacts.stream().map(contact->
                PhoneBook.builder().user(user2)
                        .phoneNumber(contact.getPhoneNumber())
                        .isCurtainCallOnAndOff(contact.getIsCurtainCallOnAndOff())
                        .nickName(contact.getName())
                        .build()).toList();
        phoneBookRepository.saveAll(phoneBooks1);
        phoneBookRepository.saveAll(phoneBooks2);


    }
}


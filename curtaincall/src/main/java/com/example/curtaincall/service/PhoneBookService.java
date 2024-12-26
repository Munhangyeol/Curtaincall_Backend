package com.example.curtaincall.service;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestRemovedNumberInPhoneBookDTO;
import com.example.curtaincall.dto.response.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.response.ResponseUserDTO;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.exception.PhoneBookNotfoundException;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import com.example.curtaincall.repository.PhoneBookRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhoneBookService {
    private final PhoneBookRepository phoneBookRepository;

    public void saveAll(List<Contact> contacts, User user){
        List<PhoneBook> phoneBooks = contacts.stream()
                .map(contact -> contact.toEntity(user, contact.getPhoneNumber()))
                .collect(Collectors.toList());
        phoneBookRepository.saveAll(phoneBooks);
    }
    public void update(Map<String, Contact> putRequestDTO,Long userId){
        for (String phoneNumber : putRequestDTO.keySet()) {
            Contact contact = putRequestDTO.get(phoneNumber);
            phoneBookRepository.updateByPhoneNumberAndUserId(contact.getIsCurtainCallOnAndOff(),
                    contact.getName(),contact.getPhoneNumber(),phoneNumber,userId);
        }
    }

    public List<ResponseUserDTO> getUserInPhoneBookAndSetOff(String phoneNumberInPhoneBook, Long userId){
        List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUserId(phoneNumberInPhoneBook, userId)
                .orElseThrow(PhoneBookNotfoundException::new);
        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(false));
        phoneBookRepository.saveAll(phoneBooks);
        return getResponseUserDTOSWithSetOff(phoneBooks);
    }


    public ResponsePhoneBookDTO getPhoneBookWithSetAllOff(CustomUserDetails userDetails){
        List<PhoneBook> phoneBooks = phoneBookRepository.findByUserId(userDetails.getId());
        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(false));
        phoneBookRepository.saveAll(phoneBooks);
        return getResponsePhoneBookDTO(userDetails.getPhoneNumber(), phoneBooks);
    }
    public void setAllOnPhoneBook(CustomUserDetails userDetails){
         phoneBookRepository.updateCurtaincallAllOnByUserId(userDetails.getId());
    }
    public void deletePhoneNumber(RequestRemovedNumberInPhoneBookDTO numbers,Long userId){
        Arrays.stream(
                numbers.removedPhoneNumber()).toList().forEach(number-> phoneBookRepository.
                deleteByPhoneNumberAndUserId(number,userId)
        );
    }
    public ResponsePhoneBookDTO findPhoneBook(CustomUserDetails userDetails){
       return getResponsePhoneBookDTO(userDetails.getPhoneNumber(), phoneBookRepository.findByUserId(userDetails.getId()));
    }
    @NotNull
    private static List<ResponseUserDTO> getResponseUserDTOSWithSetOff(List<PhoneBook> phoneBooks) {
        return phoneBooks.stream().map(phoneBook -> ResponseUserDTO.builder().nickName(phoneBook.getNickName())
                .isCurtainCallOnAndOff(false)
                .build()).collect(Collectors.toList());
    }
    @NotNull
    private ResponsePhoneBookDTO getResponsePhoneBookDTO(String phoneNumber, List<PhoneBook> phoneBooks) {
        Map<String, List<Contact>> contactMap = new HashMap<>();
        List<Contact> contacts = makeContacts(phoneBooks);
        contactMap.put(phoneNumber, contacts);
        return ResponsePhoneBookDTO.builder().response(contactMap).build();
    }
    @NotNull
    private List<Contact> makeContacts(List<PhoneBook> phoneBooks) {
        return phoneBooks.stream()
                .map(phoneBook -> Contact.builder()
                        .phoneNumber(phoneBook.getPhoneNumber())
                        .name(phoneBook.getNickName())
                        .isCurtainCallOnAndOff(phoneBook.isCurtainCallOnAndOff())
                        .build())
                .collect(Collectors.toList());
    }



}

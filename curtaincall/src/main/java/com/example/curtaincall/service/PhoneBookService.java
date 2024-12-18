package com.example.curtaincall.service;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import com.example.curtaincall.dto.Contact;
import com.example.curtaincall.dto.request.RequestRemovedNumberInPhoneBookDTO;
import com.example.curtaincall.dto.response.ResponsePhoneBookDTO;
import com.example.curtaincall.dto.response.ResponseUserDTO;
import com.example.curtaincall.global.SecretkeyManager;
import com.example.curtaincall.global.exception.PhoneBookNotfoundException;
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
    private final SecretkeyManager secretkeyManager;

    public void saveAll(List<Contact> contacts, User user){
        List<PhoneBook> phoneBooks = contacts.stream()
                .map(contact -> contact.toEntity(user, encrypt(contact.getPhoneNumber())))
                .collect(Collectors.toList());
        phoneBookRepository.saveAll(phoneBooks);
    }
    public void update(Map<String, Contact> putRequestDTO,User user){
        for (String phoneNumber : putRequestDTO.keySet()) {
            List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(encrypt(phoneNumber), user)
                    .orElseThrow(PhoneBookNotfoundException::new);
            updatePhoneBookDAO(putRequestDTO, phoneNumber, phoneBooks);
            phoneBookRepository.saveAll(phoneBooks);
        }
    }

    public List<ResponseUserDTO> getUserInPhoneBookAndSetOff(String phoneNumberInPhoneBook, User user){
        List<PhoneBook> phoneBooks = phoneBookRepository.findByPhoneNumberAndUser(encrypt(phoneNumberInPhoneBook), user)
                .orElseThrow(PhoneBookNotfoundException::new);
        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(false));
        phoneBookRepository.saveAll(phoneBooks);
        return getResponseUserDTOSWithSetOff(phoneBooks);
    }


    public ResponsePhoneBookDTO getPhoneBookWithSetAllOff(User user){
        List<PhoneBook> phoneBooks = phoneBookRepository.findByUser(user);
        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(false));
        phoneBookRepository.saveAll(phoneBooks);
        return getResponsePhoneBookDTO(user, phoneBooks);
    }
    public void setAllOnPhoneBook(User user){
        List<PhoneBook> phoneBooks = phoneBookRepository.findByUser(user);
        phoneBooks.forEach(phoneBook -> phoneBook.setCurtainCallOnAndOff(true));
        phoneBookRepository.saveAll(phoneBooks);

    }
    public List<PhoneBook> findByUser(User user){
        return phoneBookRepository.findByUser(user);
    }
    public void deletePhoneNumber(RequestRemovedNumberInPhoneBookDTO numbers,User user){
        Arrays.stream(
                numbers.removedPhoneNumber()).toList().forEach(number-> phoneBookRepository.
                deleteByPhoneNumberAndUser(encrypt(number),user)
        );
    }
    public ResponsePhoneBookDTO findPhoneBook(User user){
       return getResponsePhoneBookDTO(user, findByUser(user));
    }
    @NotNull
    private static List<ResponseUserDTO> getResponseUserDTOSWithSetOff(List<PhoneBook> phoneBooks) {
        return phoneBooks.stream().map(phoneBook -> ResponseUserDTO.builder().nickName(phoneBook.getNickName())
                .isCurtainCallOnAndOff(false)
                .build()).collect(Collectors.toList());
    }
    @NotNull

    private ResponsePhoneBookDTO getResponsePhoneBookDTO(User user, List<PhoneBook> phoneBooks) {
        Map<String, List<Contact>> contactMap = new HashMap<>();
        List<Contact> contacts = makeContacts(phoneBooks);
        contactMap.put(decrypt(user.getPhoneNumber()), contacts);
        return ResponsePhoneBookDTO.builder().response(contactMap).build();
    }
    private void updatePhoneBookDAO(Map<String, Contact> putRequestDTO, String phoneNumber, List<PhoneBook> phoneBooks) {
        for (PhoneBook phoneBook : phoneBooks) {
            Contact contact = putRequestDTO.get(phoneNumber);
            phoneBook.setPhoneNumber(encrypt(contact.getPhoneNumber()));
            phoneBook.setNickName(contact.getName());
            phoneBook.setCurtainCallOnAndOff(contact.getIsCurtainCallOnAndOff());
        }
    }
    @NotNull
    private List<Contact> makeContacts(List<PhoneBook> phoneBooks) {
        return phoneBooks.stream()
                .map(phoneBook -> Contact.builder()
                        .phoneNumber(decrypt(phoneBook.getPhoneNumber()))
                        .name(phoneBook.getNickName())
                        .isCurtainCallOnAndOff(phoneBook.isCurtainCallOnAndOff())
                        .build())
                .collect(Collectors.toList());
    }

    public String encrypt(String phoneNumber){
        return secretkeyManager.encrypt(phoneNumber);
    }
    public String decrypt(String phoneNumber){
        return secretkeyManager.decrypt(phoneNumber);
    }


}

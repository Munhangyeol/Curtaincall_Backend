package com.example.curtaincall.repository;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PhoneBookRepository extends JpaRepository<PhoneBook,Long> {
    //    @Override
    List<PhoneBook> findByUser(User user);

    Optional<List<PhoneBook>> findByPhoneNumberAndUser(String phoneNumber, User user);
}

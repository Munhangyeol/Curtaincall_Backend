package com.example.curtaincall;

import com.example.curtaincall.domain.PhoneBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneBookRepository extends JpaRepository<PhoneBook,Long> {
    //    @Override
    Optional<PhoneBook> findByPhoneNumber(String phoneNumber);
}

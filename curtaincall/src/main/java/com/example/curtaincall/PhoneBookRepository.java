package com.example.curtaincall;

import com.example.curtaincall.domain.PhoneBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneBookRepository extends JpaRepository<PhoneBook,Long> {

}

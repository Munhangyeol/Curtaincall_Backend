package com.example.curtaincall.repository;

import com.example.curtaincall.domain.PhoneBook;
import com.example.curtaincall.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PhoneBookRepository extends JpaRepository<PhoneBook,Long> {
    //    @Override
    List<PhoneBook> findByUser(User user);

    @Query("select p from PhoneBook p where p.user.id=:userId")
    List<PhoneBook> findByUserId(Long userId);
    @Query("select p from PhoneBook p where p.phoneNumber = :phoneNumber and p.user.id = :userId")
    Optional<List<PhoneBook>> findByPhoneNumberAndUserId(@Param("phoneNumber") String phoneNumber,
                                                         @Param("userId") Long userId);
    @Modifying
    @Query("update PhoneBook p " +
            "set p.isCurtainCallOnAndOff = :isCurtaincall, " +
            "p.nickName = :nickName, " +
            "p.phoneNumber = :phoneBookNumber " +
            "where p.phoneNumber = :phoneNumber and p.user.id = :userId")
    void updateByPhoneNumberAndUserId(@Param("isCurtaincall") boolean isCurtaincall,
                                      @Param("nickName") String nickName,
                                      @Param("phoneBookNumber") String phoneBookNumber,
                                      @Param("phoneNumber") String phoneNumber,
                                      @Param("userId") Long userId);
    Optional<List<PhoneBook>> findByPhoneNumberAndUser(String phoneNumber, User user);
    //spring data jpa의 동작방식과 관련이 있음-> jpql을 안쓰고는 못함
    @Modifying
    @Query("delete from PhoneBook p where p.phoneNumber=:phoneNumber and  p.user.id=:userId")
    void deleteByPhoneNumberAndUserId(String phoneNumber,Long userId);


}

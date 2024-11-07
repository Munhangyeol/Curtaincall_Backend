package com.example.curtaincall.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(UserListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String phoneNumber;
    @Column(name="nick_name")
    private String nickName;
    @Column(name="is_curtaincall_on_and_off")
    private boolean isCurtainCallOnAndOff;
    @Column(name = "user_role")
    @ColumnDefault("'USER'")
    private String userRole;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<PhoneBook> phoneBooks;

    @Builder
    public User(@NotNull String phoneNumber, String nickName, boolean isCurtainCallOnAndOff){
        this.phoneNumber=phoneNumber;
        this.nickName = nickName;
        this.isCurtainCallOnAndOff=isCurtainCallOnAndOff;
    }



}



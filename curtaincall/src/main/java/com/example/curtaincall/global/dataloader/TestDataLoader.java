package com.example.curtaincall.global.dataloader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//CommandLiner를 사용해서
//스프링 애플리케이션이 완전히 시작되고, 모든 Bean이 초기화된 상태에서 CommandLineRunner가 실행됩니다
@Component
@Profile("test")
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {
    private final UserDataLoader userDataLoader;
    private final PhoneBookDataLoader phoneBookDataLoader;


    @Transactional
    @Override
    public void run(String... args) throws Exception {
        userDataLoader.saveUser();
        phoneBookDataLoader.save();
    }
}

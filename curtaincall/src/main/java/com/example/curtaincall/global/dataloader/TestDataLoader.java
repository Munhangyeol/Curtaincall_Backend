package com.example.curtaincall.global.dataloader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

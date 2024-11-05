package com.example.curtaincall.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CurtainCallMessageService {

    @Value("${coolsms.api.key}")
    private String api_key;
    @Value("${coolsms.api.secretkey}")
    private String api_secretkey;
    @Value("${coolsms.sendPhonenumber}")
    private String sendPhoneNumber;
    private String configNumber;
    private DefaultMessageService messageService;
    private final RedisTemplate<String, String> redisTemplate;
    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(api_key, api_secretkey, "https://api.coolsms.co.kr");
    }
    public SingleMessageSentResponse makeMessageResponse(String recievePhoneNumber){
        if(isExistPhoneNumberInRedis(recievePhoneNumber))
            redisTemplate.delete(recievePhoneNumber);
        makeConfigRandomNumber(recievePhoneNumber);
        Message message = setMessage(recievePhoneNumber);
        //난수를 생성하는 session을 만들거나, db에 저장 하거나,redis를 사용.
        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    private boolean isExistPhoneNumberInRedis(String recievePhoneNumber) {
        return redisTemplate.opsForValue().get(recievePhoneNumber) != null;
    }

    public Boolean configNumber(String phoneNumber,String configNumber){
        return Objects.equals(redisTemplate.opsForValue().get(phoneNumber), configNumber);
    }
    @NotNull
    private Message setMessage(String recievePhoneNumber) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(sendPhoneNumber);
        message.setTo(recievePhoneNumber);
        message.setText("["+configNumber+"]"+"\n해당 숫자 6자리를 입력해주세요");
        return message;
    }
    private void makeConfigRandomNumber(String recievePhoneNumber) {
        Random random = new Random();
        int randomNumber = random.nextInt(900000) + 100000; // 100000 ~ 999999 범위의 숫자 생성
        configNumber=String.format("%06d", randomNumber);
        redisTemplate.opsForValue().set(recievePhoneNumber,configNumber,3, TimeUnit.MINUTES);
    }
}

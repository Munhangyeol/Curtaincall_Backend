package com.example.curtaincall.global;

import com.example.curtaincall.exception.EncryptException;
import jakarta.websocket.EncodeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class SecretkeyManager {

    @Value("${phoneNumber.secretkey}")
    private  String phoneSecretKey;
    private final String ENCRYPTION_ALGORITHM = "AES";
    public  String encrypt(String plainPhoneNumber) {
        SecretKeySpec keySpec = new SecretKeySpec(phoneSecretKey.getBytes(), ENCRYPTION_ALGORITHM);
        Cipher cipher = null;
        byte[] encrypted;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            encrypted = cipher.doFinal(plainPhoneNumber.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(encrypted);
    }
    public String decrypt(String encryptedPhoneNumber) {

        SecretKeySpec keySpec = new SecretKeySpec(phoneSecretKey.getBytes(), ENCRYPTION_ALGORITHM);
        Cipher cipher = null;
        byte[] decryptedValue;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedPhoneNumber);
            decryptedValue = cipher.doFinal(decodedValue);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return new String(decryptedValue);
    }
}

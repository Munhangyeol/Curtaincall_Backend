package com.example.curtaincall.global.exception;

public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(){

    }
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}

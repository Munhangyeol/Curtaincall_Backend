package com.example.curtaincall;

import com.example.curtaincall.dto.LogMessage;
import com.example.curtaincall.dto.response.ResponseAuthorizationDTO;
import com.example.curtaincall.global.exception.AuthorizationException;
import com.example.curtaincall.global.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvise {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AuthorizationException.class)
    public ResponseAuthorizationDTO userException(Exception e){
        return ResponseAuthorizationDTO.builder().isUser(false).message("This user is need to authorizated").build();
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public LogMessage userAlreadyException(Exception e){
        return LogMessage.builder().message(e.getMessage()).build();
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LogMessage> exception(Exception e){
        return ResponseEntity.ok(LogMessage.builder().message(String.valueOf(e)).build());
    }
}

package com.example.curtaincall;

import com.example.curtaincall.dto.LogMessage;
import com.example.curtaincall.dto.ResponseAuthorizationDTO;
import com.example.curtaincall.global.exception.AuthorizationException;
import com.example.curtaincall.global.exception.UserAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvise {
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ResponseAuthorizationDTO> userException(Exception e){
        return ResponseEntity.ok(ResponseAuthorizationDTO.builder().isUser(false).message("This user is need to authorizated").build());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<LogMessage> userAlreadyException(Exception e){
        return ResponseEntity.ok(LogMessage.builder().message(e.getMessage()).build());    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<LogMessage> exception(Exception e){
        return ResponseEntity.ok(LogMessage.builder().message(String.valueOf(e)).build());
    }
}

package com.example.curtaincall.dto;

import lombok.Builder;

public record LogMessage(String message) {
   @Builder
    public LogMessage(String message){
       this.message = message;
   }
}

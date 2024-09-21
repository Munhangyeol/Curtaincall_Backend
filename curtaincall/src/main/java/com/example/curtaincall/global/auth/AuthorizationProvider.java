package com.example.curtaincall.global.auth;

import io.jsonwebtoken.Claims;
public interface AuthorizationProvider {
    String create(CurtaincallUserInfo userInfo);

    Claims parseClaims(String token);

    boolean validateToken(String token);

    public Long getUserId(String token);
}

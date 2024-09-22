package com.example.curtaincall.global.auth.jwt;

import com.example.curtaincall.global.auth.AuthorizationProvider;
import com.example.curtaincall.global.auth.CurtaincallUserInfo;
import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils implements AuthorizationProvider {

    private final Key key;
    private final long accessTokenExpTime;

    public JwtUtils(@Value("${curtaincall.auth.jwt.secret}") String secretKey,
                   @Value("${curtaincall.auth.jwt.expireTime}") long accessTokenExpTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
    }

    @Override
    public String create(CurtaincallUserInfo userInfo) {
        Claims claims= Jwts.claims();
        claims.put("userId", userInfo.getId());
        claims.put("userPhoneNumber",userInfo.getPhoneNumber());
        claims.put("role", userInfo.getRole());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(accessTokenExpTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Override
    public boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    @Override
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        Object memberId = claims.get("userId");

        if (memberId instanceof Number) {
            return ((Number) memberId).longValue();
        } else {
            throw new IllegalStateException("Invalid userId type in JWT");
        }
    }
}

package com.example.curtaincall.global;

import com.example.curtaincall.global.auth.jwt.JwtUtils;
import com.example.curtaincall.global.userDetail.CustomUserDetailService;
import com.example.curtaincall.global.userDetail.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TokenManager {
    public JwtUtils jwtUtils;
    public CustomUserDetailService customUserDetailsService;
    public String phoneNumberBytoken(String token){
        Long id = jwtUtils.getUserId(token);
        return customUserDetailsService.loadUserById(id).getPhoneNumber();
    }
    public String getPhoneNumberByToken(HttpServletRequest request){
        String phoneNumber = "";
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtUtils.validateToken(token)) {
                Long userId = jwtUtils.getUserId(token);
                CustomUserDetails userDetails = customUserDetailsService.loadUserById(userId);
                if (userDetails != null) {
                    phoneNumber = userDetails.getPhoneNumber(); // 휴대폰 번호 가져오기
                    System.out.println("lohe"+phoneNumber);

                }
            }
        }
    return phoneNumber;
    }
}

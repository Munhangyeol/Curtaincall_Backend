package com.example.curtaincall.global.userDetail;

import com.example.curtaincall.domain.User;
import com.example.curtaincall.global.auth.CurtaincallUserInfo;
import com.example.curtaincall.global.exception.UserNotfoundException;
import com.example.curtaincall.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
    public CustomUserDetails loadUserById(Long id){
        User user=userRepository.findById(id).orElseThrow(UserNotfoundException::new);
        CurtaincallUserInfo userInfo=CurtaincallUserInfo.builder().role(user.getUserRole())
                .phoneNumber(user.getPhoneNumber())
                .isCurtaincall(user.isCurtainCallOnAndOff())
                .nickName(user.getNickName()).build();
        return new CustomUserDetails(userInfo);
    }
}

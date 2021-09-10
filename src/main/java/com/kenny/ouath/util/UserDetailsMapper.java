package com.kenny.ouath.util;

import com.kenny.ouath.model.LoginUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsMapper {

    public static UserDetails toUserDetails(LoginUser loginUser) {

        return User.withUsername(loginUser.getEmail())
                .password(loginUser.getPassword())
                .roles(loginUser.getRoles().stream().toArray(String[]::new))
                .build();
    }
}


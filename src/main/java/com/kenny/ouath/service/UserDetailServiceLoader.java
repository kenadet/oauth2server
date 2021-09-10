package com.kenny.ouath.service;


import com.kenny.ouath.model.LoginUser;
import com.kenny.ouath.util.UserDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceLoader implements UserDetailsService {

    private final UserService userService;
    private final UserDetailsMapper userDetailsMapper;

    @Autowired
    public UserDetailServiceLoader(UserService userService, UserDetailsMapper userDetailsMapper) {

        this.userService = userService;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email){

        LoginUser signUpUser = userService.getUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email not found"));

        return userDetailsMapper.toUserDetails(signUpUser);
    }

}


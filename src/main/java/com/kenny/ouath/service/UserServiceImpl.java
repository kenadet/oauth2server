package com.kenny.ouath.service;

import com.kenny.ouath.model.LoginUser;
import com.kenny.ouath.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginUser save(LoginUser signUpUser) {
        return userRepository.save(signUpUser);
    }

    @Override
    public Optional<LoginUser> getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }
}

package com.kenny.ouath.service;

import com.kenny.ouath.model.LoginUser;
import java.util.Optional;

public interface UserService {

    LoginUser save(LoginUser loginUser);

    Optional<LoginUser> getUserByEmail(String phone);

}

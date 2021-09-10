package com.kenny.ouath.repository;

import com.kenny.ouath.model.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<LoginUser, UUID> {

    Optional<LoginUser> findByEmail(String email);

}

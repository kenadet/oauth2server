package com.kenny.ouath.exception;

public class AccountAlreadyExist extends RuntimeException {

    public AccountAlreadyExist(String message) {
        super(message);
    }
}

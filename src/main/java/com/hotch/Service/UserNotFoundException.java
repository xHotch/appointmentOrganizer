package com.hotch.Service;

public class UserNotFoundException extends Exception {
    UserNotFoundException(String message) {
        super(message);
    }

}

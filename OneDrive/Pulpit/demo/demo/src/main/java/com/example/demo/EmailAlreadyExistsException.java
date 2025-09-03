package com.example.demo;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String email) {
        super("Email: " + email + " already in use");
    }
}

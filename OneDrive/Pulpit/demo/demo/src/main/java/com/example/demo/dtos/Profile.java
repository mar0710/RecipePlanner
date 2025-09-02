package com.example.demo.dtos;

import lombok.Data;

@Data
public class Profile {
    private String username;
    private  String email;

    public Profile() {
    }

    public Profile(String username, String email) {
        this.username = username;
        this.email = email;
    }
}

package com.example.demo.model;

import java.util.Objects;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class User {

    private @Id @GeneratedValue Long id;
    private String userName;
    private String email;
    private String password;

    public User() {

    }
    public User(String userName, String email, String password) {

        this.userName = userName;
        this.email = email;
        this.password = password;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    @Column(name = "username", nullable = false)
    public String getUserName() { return userName; }
    public void setUserName(String name) { this.userName = name; }
    @Column(name = "email", nullable = false)
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    @Column(name = "password", nullable = false)
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", name='" + this.userName + '\'' + '\'' + '}';
    }

}
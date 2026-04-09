package com.example.dto;

public class LoginRequest {
    private String username;
    private String password;

    // Getters and Setters are MANDATORY for Spring to map the JSON
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
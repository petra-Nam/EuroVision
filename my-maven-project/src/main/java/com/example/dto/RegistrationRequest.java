package com.example.dto;

public class RegistrationRequest {
    private String username;
    private String password;
    private String countryName;
    private boolean isJury;
    private String jobTitle; // For Jury role

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getCountryName() { return countryName; }
    public void setCountryName(String countryName) { this.countryName = countryName; }
    public boolean isIsJury() { return isJury; }
    public void setIsJury(boolean isJury) { this.isJury = isJury; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
}
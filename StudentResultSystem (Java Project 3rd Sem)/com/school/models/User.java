package com.school.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password;
    private String role; // "ADMIN" or "TEACHER"
    private String email;
    private LocalDateTime registrationDate;
    
    public User(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.registrationDate = LocalDateTime.now();
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    
    public boolean authenticate(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return String.format("User: %s [%s] | Email: %s | Registered: %s", 
                           username, role, email, registrationDate.format(formatter));
    }
}
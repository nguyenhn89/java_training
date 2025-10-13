package org.example.java_training.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
//import jakarta.validation.constraints.Size;
//import jakarta.validation.constraints.Email;

public class AuthRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3–50 characters")
    private String userName;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    public AuthRequest() {}

    public AuthRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

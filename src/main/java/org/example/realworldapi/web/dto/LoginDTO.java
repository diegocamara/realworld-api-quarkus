package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

import javax.validation.constraints.NotBlank;

@JsonRootName("user")
public class LoginDTO {

    @NotBlank(message = "email must be not blank")
    private String email;
    @NotBlank(message = "password must be not blank")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

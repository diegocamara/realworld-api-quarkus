package org.example.realworldapi.web.dto;

import org.example.realworldapi.domain.entity.User;

public class UpdateUserDTO {

    private String username;
    private String bio;
    private String image;
    private String email;

    public User toUser(Long id){
        User user = new User();
        user.setId(id);
        user.setUsername(this.username);
        user.setBio(this.bio);
        user.setImage(this.image);
        user.setEmail(this.email);
        return user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

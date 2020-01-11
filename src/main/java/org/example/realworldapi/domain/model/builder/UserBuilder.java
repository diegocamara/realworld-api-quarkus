package org.example.realworldapi.domain.model.builder;

import org.example.realworldapi.domain.model.entity.User;

public class UserBuilder {

    private Long id;
    private String username;
    private String bio;
    private String image;
    private String password;
    private String email;
    private String token;

    public UserBuilder id(Long id){
        this.id = id;
        return this;
    }

    public UserBuilder username(String username){
        this.username = username;
        return this;
    }

    public UserBuilder bio(String bio){
        this.bio = bio;
        return this;
    }

    public UserBuilder image(String image){
        this.image = image;
        return this;
    }

    public UserBuilder password(String password){
        this.password = password;
        return this;
    }

    public UserBuilder email(String email){
        this.email = email;
        return this;
    }

    public UserBuilder token(String token){
        this.token = token;
        return this;
    }

    public User build(){
        User user = new User();
        user.setId(this.id);
        user.setUsername(this.username);
        user.setBio(this.bio);
        user.setImage(this.image);
        user.setPassword(this.password);
        user.setEmail(this.email);
        user.setToken(this.token);
        return user;
    }

}

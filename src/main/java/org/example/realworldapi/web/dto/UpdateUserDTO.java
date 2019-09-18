package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.domain.entity.User;

@Getter
@Setter
@JsonRootName("user")
@RegisterForReflection
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

}

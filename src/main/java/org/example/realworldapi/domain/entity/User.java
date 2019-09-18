package org.example.realworldapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@JsonRootName("user")
@Entity
@Table(name = "USERS")
@RegisterForReflection
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long id;

    private String username;
    private String bio;
    private String image;
    @JsonIgnore
    private String password;
    private String email;

    @Column(length = 500)
    private String token;

}

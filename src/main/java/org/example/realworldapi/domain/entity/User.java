package org.example.realworldapi.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
  @JsonIgnore private String password;
  private String email;

  @Column(length = 500)
  private String token;

  @JsonIgnore
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "USERS_FOLLOWERS")
  private List<User> follows;

  public User(Long id, String username, String bio, String image) {
    this.id = id;
    this.username = username;
    this.bio = bio;
    this.image = image;
  }
}

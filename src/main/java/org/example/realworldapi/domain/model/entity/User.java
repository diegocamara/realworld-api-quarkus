package org.example.realworldapi.domain.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USERS")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String username;
  private String bio;
  private String image;
  private String password;
  private String email;

  @Column(length = 500)
  private String token;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private List<Article> articles;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<UsersFollowed> following;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "followed")
  private List<UsersFollowed> followedBy;

  public User(Long id, String username, String bio, String image) {
    this.id = id;
    this.username = username;
    this.bio = bio;
    this.image = image;
  }
}

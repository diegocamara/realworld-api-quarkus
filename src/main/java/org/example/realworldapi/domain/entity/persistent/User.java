package org.example.realworldapi.domain.entity.persistent;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedList;
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

  //  @JsonIgnore
  //  @OneToMany(mappedBy = "primaryKey.user", fetch = FetchType.LAZY)
  //  private List<UsersFollowers> followers;

  @OneToMany(mappedBy = "author", orphanRemoval = true)
  private List<Article> articles = new LinkedList<>();

  public User(Long id, String username, String bio, String image) {
    this.id = id;
    this.username = username;
    this.bio = bio;
    this.image = image;
  }
}

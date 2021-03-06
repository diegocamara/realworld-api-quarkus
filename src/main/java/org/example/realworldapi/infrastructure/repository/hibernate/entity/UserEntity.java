package org.example.realworldapi.infrastructure.repository.hibernate.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.realworldapi.domain.model.user.User;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "NEWUSERS")
public class UserEntity {

  @Id private UUID id;

  private String username;
  private String bio;
  private String image;
  private String password;
  private String email;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id")
  private List<ArticleEntity> articles;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<UsersFollowedEntity> following;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "followed")
  private List<UsersFollowedEntity> followedBy;

  public UserEntity(User user) {
    this.id = user.getId();
    update(user);
  }

  public void update(User user) {
    this.username = user.getUsername();
    this.bio = user.getBio();
    this.image = user.getImage();
    this.password = user.getPassword();
    this.email = user.getEmail();
  }
}

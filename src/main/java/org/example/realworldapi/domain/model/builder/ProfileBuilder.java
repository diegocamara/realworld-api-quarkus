package org.example.realworldapi.domain.model.builder;

import org.example.realworldapi.application.data.ProfileData;
import org.example.realworldapi.domain.model.entity.User;

public class ProfileBuilder {

  private String username;
  private String bio;
  private String image;
  private boolean following;

  public ProfileBuilder username(String username) {
    this.username = username;
    return this;
  }

  public ProfileBuilder bio(String bio) {
    this.bio = bio;
    return this;
  }

  public ProfileBuilder image(String image) {
    this.image = image;
    return this;
  }

  public ProfileBuilder following(boolean following) {
    this.following = following;
    return this;
  }

  public ProfileData build() {
    return new ProfileData(this.username, this.bio, this.image, this.following);
  }

  public ProfileBuilder fromUser(User existentUser) {
    return this.username(existentUser.getUsername())
        .bio(existentUser.getBio())
        .image(existentUser.getImage());
  }
}

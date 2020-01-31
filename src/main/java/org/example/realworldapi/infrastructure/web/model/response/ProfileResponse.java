package org.example.realworldapi.infrastructure.web.model.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;
import org.example.realworldapi.application.data.ProfileData;

@Getter
@Setter
@JsonRootName("profile")
@RegisterForReflection
public class ProfileResponse {

  private String username;
  private String bio;
  private String image;
  private boolean following;

  public ProfileResponse(ProfileData profile) {
    this.username = profile.getUsername();
    this.bio = profile.getBio();
    this.image = profile.getImage();
    this.following = profile.isFollowing();
  }
}

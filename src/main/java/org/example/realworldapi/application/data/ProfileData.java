package org.example.realworldapi.application.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProfileData {

  private String username;
  private String bio;
  private String image;
  private boolean following;
}

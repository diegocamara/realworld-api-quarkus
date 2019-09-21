package org.example.realworldapi.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Profile {

  private String username;
  private String bio;
  private String image;
  private boolean following;
}

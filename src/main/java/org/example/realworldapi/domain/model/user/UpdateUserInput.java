package org.example.realworldapi.domain.model.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UpdateUserInput {
  private UUID id;
  private String username;
  private String bio;
  private String image;
  private String email;
}

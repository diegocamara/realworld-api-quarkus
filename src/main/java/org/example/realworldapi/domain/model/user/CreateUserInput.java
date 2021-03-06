package org.example.realworldapi.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserInput {
  private String username;
  private String email;
  private String password;
}

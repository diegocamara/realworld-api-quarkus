package org.example.realworldapi.domain.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserInput {
  private String email;
  private String password;
}

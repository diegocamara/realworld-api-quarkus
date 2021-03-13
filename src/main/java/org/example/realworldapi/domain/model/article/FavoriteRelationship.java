package org.example.realworldapi.domain.model.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.realworldapi.domain.model.user.User;

@Data
@AllArgsConstructor
public class FavoriteRelationship {
  private User user;
  private Article article;
}

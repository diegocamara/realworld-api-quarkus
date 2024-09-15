package org.example.realworldapi.domain.model.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.realworldapi.domain.model.article.Article;
import org.example.realworldapi.domain.model.user.User;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
  @NotNull private final UUID id;
  @NotNull private final User author;
  @NotNull private final Article article;
  @NotBlank private final String body;
  @NotNull private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
}

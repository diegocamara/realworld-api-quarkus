package org.example.realworldapi.domain.model.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.domain.model.user.User;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {
  @NotNull private UUID id;
  @NotBlank private String slug;

  @NotBlank(message = ValidationMessages.TITLE_MUST_BE_NOT_BLANK)
  private String title;

  @NotBlank(message = ValidationMessages.DESCRIPTION_MUST_BE_NOT_BLANK)
  private String description;

  @NotBlank(message = ValidationMessages.BODY_MUST_BE_NOT_BLANK)
  private String body;

  @NotNull private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  @NotNull private User author;
}

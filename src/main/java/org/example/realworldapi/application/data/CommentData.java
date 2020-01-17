package org.example.realworldapi.application.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentData {

  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String body;
  private ProfileData author;
}

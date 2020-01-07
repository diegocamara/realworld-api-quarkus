package org.example.realworldapi.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  private Long id;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private String body;
  private Profile author;
}

package org.example.realworldapi.domain.model.tag;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
  @NotNull private UUID id;
  @NotBlank private String name;
}

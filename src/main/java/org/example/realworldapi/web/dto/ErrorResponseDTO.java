package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@JsonRootName("errors")
@RegisterForReflection
public class ErrorResponseDTO {

  private List<String> body;

  public ErrorResponseDTO() {
    this.body = new LinkedList<>();
  }

  public ErrorResponseDTO(String error) {
    this();
    this.body.add(error);
  }
}

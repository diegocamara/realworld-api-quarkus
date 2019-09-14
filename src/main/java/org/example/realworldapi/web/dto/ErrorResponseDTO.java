package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.LinkedList;
import java.util.List;

@JsonRootName("errors")
@RegisterForReflection
public class ErrorResponseDTO {

    private List<String> body;

    public ErrorResponseDTO(){
        this.body = new LinkedList<>();
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }
}

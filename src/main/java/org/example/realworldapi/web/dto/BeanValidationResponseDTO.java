package org.example.realworldapi.web.dto;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.LinkedList;
import java.util.List;

@JsonRootName("errors")
public class BeanValidationResponseDTO {

    private List<String> body;

    public BeanValidationResponseDTO(){
        this.body = new LinkedList<>();
    }

    public List<String> getBody() {
        return body;
    }

    public void setBody(List<String> body) {
        this.body = body;
    }
}

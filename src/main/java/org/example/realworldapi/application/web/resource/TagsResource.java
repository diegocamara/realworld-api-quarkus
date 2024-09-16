package org.example.realworldapi.application.web.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import lombok.AllArgsConstructor;
import org.example.realworldapi.application.web.model.response.TagsResponse;
import org.example.realworldapi.domain.feature.FindTags;
import org.example.realworldapi.domain.model.tag.Tag;
import org.example.realworldapi.infrastructure.web.qualifiers.NoWrapRootValueObjectMapper;

@Path("/tags")
@AllArgsConstructor
public class TagsResource {

  @NoWrapRootValueObjectMapper ObjectMapper objectMapper;
  private final FindTags findTags;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTags() throws JsonProcessingException {
    List<Tag> tags = findTags.handle();
    return Response.ok(objectMapper.writeValueAsString(new TagsResponse(tags)))
        .status(Response.Status.OK)
        .build();
  }
}

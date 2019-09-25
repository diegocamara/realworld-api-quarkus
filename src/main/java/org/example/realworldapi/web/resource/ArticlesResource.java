package org.example.realworldapi.web.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.realworldapi.domain.entity.Article;
import org.example.realworldapi.domain.resource.service.ArticlesService;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.web.dto.ArticlesDTO;
import org.example.realworldapi.web.qualifiers.NoWrapRootValueObjectMapper;
import org.example.realworldapi.web.security.annotation.Secured;

import javax.validation.constraints.Min;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/articles")
public class ArticlesResource {

  private ArticlesService articlesService;
  private ObjectMapper objectMapper;

  public ArticlesResource(
      ArticlesService articlesService, @NoWrapRootValueObjectMapper ObjectMapper objectMapper) {
    this.articlesService = articlesService;
    this.objectMapper = objectMapper;
  }

  @GET
  @Path("/feed")
  @Secured({Role.USER, Role.ADMIN})
  @Produces(MediaType.APPLICATION_JSON)
  public Response feed(
      @QueryParam("offset") int offset,
      @QueryParam("limit") @Min(value = 1, message = "limit parameter must be at least 1")
          int limit,
      @Context SecurityContext securityContext)
      throws JsonProcessingException {
    Long loggedUserId = Long.valueOf(securityContext.getUserPrincipal().getName());
    List<Article> articles = articlesService.findRecentArticles(loggedUserId, offset, limit);
    return Response.ok(objectMapper.writeValueAsString(new ArticlesDTO(articles)))
        .status(Response.Status.OK)
        .build();
  }
}

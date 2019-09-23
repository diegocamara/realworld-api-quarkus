package org.example.realworldapi.web.resource;

import org.example.realworldapi.domain.entity.persistent.Article;
import org.example.realworldapi.domain.resource.service.ArticlesService;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.web.dto.ArticlesDTO;
import org.example.realworldapi.web.security.annotation.Secured;

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

  public ArticlesResource(ArticlesService articlesService) {
    this.articlesService = articlesService;
  }

  @GET
  @Path("/feed")
  @Secured({Role.USER, Role.ADMIN})
  @Produces(MediaType.APPLICATION_JSON)
  public Response feed(
      @QueryParam("offset") int offset,
      @QueryParam("limit") int limit,
      @Context SecurityContext securityContext) {
    Long loggedUserId = Long.valueOf(securityContext.getUserPrincipal().getName());
    List<Article> articles = articlesService.findRecentArticles(loggedUserId, offset, limit);
    return Response.ok(new ArticlesDTO(articles)).status(Response.Status.OK).build();
  }
}

package org.example.realworldapi.web.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.realworldapi.domain.entity.Article;
import org.example.realworldapi.domain.resource.service.ArticlesService;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.web.dto.ArticleDTO;
import org.example.realworldapi.web.dto.ArticlesDTO;
import org.example.realworldapi.web.dto.NewArticleDTO;
import org.example.realworldapi.web.dto.UpdateArticleDTO;
import org.example.realworldapi.web.qualifiers.NoWrapRootValueObjectMapper;
import org.example.realworldapi.web.security.annotation.Secured;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
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
      @QueryParam("limit") int limit,
      @Context SecurityContext securityContext)
      throws JsonProcessingException {
    Long loggedUserId = Long.valueOf(securityContext.getUserPrincipal().getName());
    List<Article> articles = articlesService.findRecentArticles(loggedUserId, offset, limit);
    return Response.ok(objectMapper.writeValueAsString(new ArticlesDTO(articles)))
        .status(Response.Status.OK)
        .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Secured(optional = true)
  public Response getArticles(
      @QueryParam("offset") int offset,
      @QueryParam("limit") int limit,
      @QueryParam("tag") List<String> tags,
      @QueryParam("author") List<String> authors,
      @QueryParam("favorited") List<String> favorited,
      @Context SecurityContext securityContext)
      throws JsonProcessingException {

    Long loggedUserId =
        securityContext.getUserPrincipal() != null
            ? Long.valueOf(securityContext.getUserPrincipal().getName())
            : null;

    List<Article> articles =
        articlesService.findArticles(offset, limit, loggedUserId, tags, authors, favorited);
    return Response.ok(objectMapper.writeValueAsString(new ArticlesDTO(articles)))
        .status(Response.Status.OK)
        .build();
  }

  @POST
  @Secured({Role.ADMIN, Role.USER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(
      @Valid @NotNull(message = "request body must be not null") NewArticleDTO newArticleDTO,
      @Context SecurityContext securityContext) {
    Long loggedUserId = Long.valueOf(securityContext.getUserPrincipal().getName());
    Article newArticle =
        articlesService.create(
            newArticleDTO.getTitle(),
            newArticleDTO.getDescription(),
            newArticleDTO.getBody(),
            newArticleDTO.getTagList(),
            loggedUserId);
    return Response.ok(new ArticleDTO(newArticle)).status(Response.Status.CREATED).build();
  }

  @GET
  @Path("/{slug}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findBySlug(@PathParam("slug") @NotBlank String slug) {
    Article article = articlesService.findBySlug(slug);
    return Response.ok(new ArticleDTO(article)).status(Response.Status.OK).build();
  }

  @PUT
  @Path("/{slug}")
  @Secured({Role.ADMIN, Role.USER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(
      @PathParam("slug") @NotBlank String slug,
      @Valid @NotNull UpdateArticleDTO updateArticleDTO,
      @Context SecurityContext securityContext) {

    Long loggedUserId = Long.valueOf(securityContext.getUserPrincipal().getName());

    Article updatedArticle =
        articlesService.update(
            slug,
            updateArticleDTO.getTitle(),
            updateArticleDTO.getDescription(),
            updateArticleDTO.getBody(),
            loggedUserId);
    return Response.ok(new ArticleDTO(updatedArticle)).status(Response.Status.OK).build();
  }
}

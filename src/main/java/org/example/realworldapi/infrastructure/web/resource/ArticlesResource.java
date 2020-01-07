package org.example.realworldapi.infrastructure.web.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.domain.model.entity.Article;
import org.example.realworldapi.domain.model.entity.Articles;
import org.example.realworldapi.domain.model.entity.Comment;
import org.example.realworldapi.domain.service.ArticlesService;
import org.example.realworldapi.infrastructure.web.security.profile.Role;
import org.example.realworldapi.infrastructure.web.model.request.NewArticleRequest;
import org.example.realworldapi.infrastructure.web.model.request.NewCommentRequest;
import org.example.realworldapi.infrastructure.web.model.request.UpdateArticleRequest;
import org.example.realworldapi.infrastructure.web.model.response.ArticleResponse;
import org.example.realworldapi.infrastructure.web.model.response.ArticlesResponse;
import org.example.realworldapi.infrastructure.web.model.response.CommentResponse;
import org.example.realworldapi.infrastructure.web.model.response.CommentsResponse;
import org.example.realworldapi.infrastructure.web.qualifiers.NoWrapRootValueObjectMapper;
import org.example.realworldapi.infrastructure.web.security.annotation.Secured;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
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
    Long loggedUserId = getLoggedUserId(securityContext);
    Articles result = articlesService.findRecentArticles(loggedUserId, offset, limit);
    return Response.ok(objectMapper.writeValueAsString(new ArticlesResponse(result)))
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
    Long loggedUserId = getLoggedUserId(securityContext);
    Articles result =
        articlesService.findArticles(offset, limit, loggedUserId, tags, authors, favorited);
    return Response.ok(objectMapper.writeValueAsString(new ArticlesResponse(result)))
        .status(Response.Status.OK)
        .build();
  }

  @POST
  @Secured({Role.ADMIN, Role.USER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(
      @Valid @NotNull(message = ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL)
          NewArticleRequest newArticleRequest,
      @Context SecurityContext securityContext) {
    Long loggedUserId = getLoggedUserId(securityContext);
    Article newArticle =
        articlesService.create(
            newArticleRequest.getTitle(),
            newArticleRequest.getDescription(),
            newArticleRequest.getBody(),
            newArticleRequest.getTagList(),
            loggedUserId);
    return Response.ok(new ArticleResponse(newArticle)).status(Response.Status.CREATED).build();
  }

  @GET
  @Path("/{slug}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response findBySlug(
      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK)
          String slug) {
    Article article = articlesService.findBySlug(slug);
    return Response.ok(new ArticleResponse(article)).status(Response.Status.OK).build();
  }

  @PUT
  @Path("/{slug}")
  @Secured({Role.ADMIN, Role.USER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(
      @PathParam("slug") @NotBlank String slug,
      @Valid @NotNull UpdateArticleRequest updateArticleRequest,
      @Context SecurityContext securityContext) {
    Long loggedUserId = getLoggedUserId(securityContext);
    Article updatedArticle =
        articlesService.update(
            slug,
            updateArticleRequest.getTitle(),
            updateArticleRequest.getDescription(),
            updateArticleRequest.getBody(),
            loggedUserId);
    return Response.ok(new ArticleResponse(updatedArticle)).status(Response.Status.OK).build();
  }

  @DELETE
  @Path("/{slug}")
  @Secured({Role.ADMIN, Role.USER})
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(
      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String slug,
      @Context SecurityContext securityContext) {
    Long loggedUserId = getLoggedUserId(securityContext);
    articlesService.delete(slug, loggedUserId);
    return Response.ok().build();
  }

  @GET
  @Path("/{slug}/comments")
  @Secured(optional = true)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCommentsBySlug(
      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String slug,
      @Context SecurityContext securityContext)
      throws JsonProcessingException {
    Long loggedUserId = getLoggedUserId(securityContext);
    List<Comment> comments = articlesService.findCommentsBySlug(slug, loggedUserId);
    return Response.ok(objectMapper.writeValueAsString(new CommentsResponse(comments)))
        .status(Response.Status.OK)
        .build();
  }

  @POST
  @Path("/{slug}/comments")
  @Secured({Role.ADMIN, Role.USER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createComment(
      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String slug,
      @Valid NewCommentRequest newCommentRequest,
      @Context SecurityContext securityContext) {
    Long loggedUserId = getLoggedUserId(securityContext);
    Comment comment =
        articlesService.createComment(slug, newCommentRequest.getBody(), loggedUserId);
    return Response.ok(new CommentResponse(comment)).status(Response.Status.OK).build();
  }

  @DELETE
  @Path("/{slug}/comments/{id}")
  @Secured({Role.ADMIN, Role.USER})
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteComment(
      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String slug,
      @PathParam("id") @NotNull(message = ValidationMessages.COMMENT_ID_MUST_BE_NOT_NULL) Long id,
      @Context SecurityContext securityContext) {
    Long loggedUserId = getLoggedUserId(securityContext);
    articlesService.deleteComment(slug, id, loggedUserId);
    return Response.ok().build();
  }

  @POST
  @Path("/{slug}/favorite")
  @Secured({Role.ADMIN, Role.USER})
  @Produces(MediaType.APPLICATION_JSON)
  public Response favoriteArticle(
      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String slug,
      @Context SecurityContext securityContext) {
    Long loggedUserId = getLoggedUserId(securityContext);
    Article article = articlesService.favoriteArticle(slug, loggedUserId);
    return Response.ok(new ArticleResponse(article)).status(Response.Status.OK).build();
  }

  @DELETE
  @Path("/{slug}/favorite")
  @Secured({Role.ADMIN, Role.USER})
  @Produces(MediaType.APPLICATION_JSON)
  public Response unfavoriteArticle(
      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String slug,
      @Context SecurityContext securityContext) {
    Long loggedUserId = getLoggedUserId(securityContext);
    Article article = articlesService.unfavoriteArticle(slug, loggedUserId);
    return Response.ok(new ArticleResponse(article)).status(Response.Status.OK).build();
  }

  private Long getLoggedUserId(SecurityContext securityContext) {
    Principal principal = securityContext.getUserPrincipal();
    return principal != null ? Long.valueOf(principal.getName()) : null;
  }
}

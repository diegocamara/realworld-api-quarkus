package org.example.realworldapi.application.web.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.example.realworldapi.application.utils.ResourceUtils;
import org.example.realworldapi.application.web.model.request.NewArticleRequest;
import org.example.realworldapi.domain.feature.CreateArticle;
import org.example.realworldapi.domain.feature.FindArticleTags;
import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.domain.service.ArticlesService;
import org.example.realworldapi.infrastructure.web.qualifiers.NoWrapRootValueObjectMapper;
import org.example.realworldapi.infrastructure.web.security.annotation.Secured;
import org.example.realworldapi.infrastructure.web.security.profile.Role;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/articles")
@AllArgsConstructor
public class ArticlesResource {

  private final CreateArticle createArticle;
  private final FindArticleTags findArticleTags;
  private final ArticlesService articlesService;
  @NoWrapRootValueObjectMapper ObjectMapper objectMapper;
  private final ResourceUtils resourceUtils;

  //  @GET
  //  @Path("/feed")
  //  @Secured({Role.USER, Role.ADMIN})
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response feed(
  //      @QueryParam("offset") int offset,
  //      @QueryParam("limit") int limit,
  //      @Context SecurityContext securityContext)
  //      throws JsonProcessingException {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    ArticlesData result = articlesService.findRecentArticles(loggedUserId, offset, limit);
  //    return Response.ok(objectMapper.writeValueAsString(new ArticlesResponse(result)))
  //        .status(Response.Status.OK)
  //        .build();
  //  }

  //  @GET
  //  @Produces(MediaType.APPLICATION_JSON)
  //  @Secured(optional = true)
  //  public Response getArticles(
  //      @QueryParam("offset") int offset,
  //      @QueryParam("limit") int limit,
  //      @QueryParam("tag") List<String> tags,
  //      @QueryParam("author") List<String> authors,
  //      @QueryParam("favorited") List<String> favorited,
  //      @Context SecurityContext securityContext)
  //      throws JsonProcessingException {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    ArticlesData result =
  //        articlesService.findArticles(offset, limit, loggedUserId, tags, authors, favorited);
  //    return Response.ok(objectMapper.writeValueAsString(new ArticlesResponse(result)))
  //        .status(Response.Status.OK)
  //        .build();
  //  }

  @POST
  @Transactional
  @Secured({Role.ADMIN, Role.USER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(
      @Valid @NotNull(message = ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL)
          NewArticleRequest newArticleRequest,
      @Context SecurityContext securityContext) {
    final var loggedUserId = resourceUtils.getLoggedUserId(securityContext);
    final var article = createArticle.handle(newArticleRequest.toNewArticleInput(loggedUserId));
    //    ArticleData newArticleData =
    //        articlesService.create(
    //            newArticleRequest.getTitle(),
    //            newArticleRequest.getDescription(),
    //            newArticleRequest.getBody(),
    //            newArticleRequest.getTagList(),
    //            loggedUserId);
    return Response.ok(resourceUtils.getArticleResponse(article, loggedUserId))
        .status(Response.Status.CREATED)
        .build();
  }

  //  @GET
  //  @Path("/{slug}")
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response findBySlug(
  //      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK)
  //          String slug) {
  //    ArticleData articleData = articlesService.findBySlug(slug);
  //    return Response.ok(new ArticleResponse(articleData)).status(Response.Status.OK).build();
  //  }

  //  @PUT
  //  @Path("/{slug}")
  //  @Secured({Role.ADMIN, Role.USER})
  //  @Consumes(MediaType.APPLICATION_JSON)
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response update(
  //      @PathParam("slug") @NotBlank String slug,
  //      @Valid @NotNull UpdateArticleRequest updateArticleRequest,
  //      @Context SecurityContext securityContext) {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    ArticleData updatedArticleData =
  //        articlesService.update(
  //            slug,
  //            updateArticleRequest.getTitle(),
  //            updateArticleRequest.getDescription(),
  //            updateArticleRequest.getBody(),
  //            loggedUserId);
  //    return Response.ok(new
  // ArticleResponse(updatedArticleData)).status(Response.Status.OK).build();
  //  }

  //  @DELETE
  //  @Path("/{slug}")
  //  @Secured({Role.ADMIN, Role.USER})
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response delete(
  //      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String
  // slug,
  //      @Context SecurityContext securityContext) {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    articlesService.delete(slug, loggedUserId);
  //    return Response.ok().build();
  //  }

  //  @GET
  //  @Path("/{slug}/comments")
  //  @Secured(optional = true)
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response getCommentsBySlug(
  //      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String
  // slug,
  //      @Context SecurityContext securityContext)
  //      throws JsonProcessingException {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    List<CommentData> comments = articlesService.findCommentsBySlug(slug, loggedUserId);
  //    return Response.ok(objectMapper.writeValueAsString(new CommentsResponse(comments)))
  //        .status(Response.Status.OK)
  //        .build();
  //  }

  //  @POST
  //  @Path("/{slug}/comments")
  //  @Secured({Role.ADMIN, Role.USER})
  //  @Consumes(MediaType.APPLICATION_JSON)
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response createComment(
  //      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String
  // slug,
  //      @Valid NewCommentRequest newCommentRequest,
  //      @Context SecurityContext securityContext) {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    CommentData commentData =
  //        articlesService.createComment(slug, newCommentRequest.getBody(), loggedUserId);
  //    return Response.ok(new CommentResponse(commentData)).status(Response.Status.OK).build();
  //  }

  //  @DELETE
  //  @Path("/{slug}/comments/{id}")
  //  @Secured({Role.ADMIN, Role.USER})
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response deleteComment(
  //      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String
  // slug,
  //      @PathParam("id") @NotNull(message = ValidationMessages.COMMENT_ID_MUST_BE_NOT_NULL) Long
  // id,
  //      @Context SecurityContext securityContext) {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    articlesService.deleteComment(slug, id, loggedUserId);
  //    return Response.ok().build();
  //  }

  //  @POST
  //  @Path("/{slug}/favorite")
  //  @Secured({Role.ADMIN, Role.USER})
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response favoriteArticle(
  //      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String
  // slug,
  //      @Context SecurityContext securityContext) {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    ArticleData articleData = articlesService.favoriteArticle(slug, loggedUserId);
  //    return Response.ok(new ArticleResponse(articleData)).status(Response.Status.OK).build();
  //  }

  //  @DELETE
  //  @Path("/{slug}/favorite")
  //  @Secured({Role.ADMIN, Role.USER})
  //  @Produces(MediaType.APPLICATION_JSON)
  //  public Response unfavoriteArticle(
  //      @PathParam("slug") @NotBlank(message = ValidationMessages.SLUG_MUST_BE_NOT_BLANK) String
  // slug,
  //      @Context SecurityContext securityContext) {
  //    Long loggedUserId = getLoggedUserId(securityContext);
  //    ArticleData articleData = articlesService.unfavoriteArticle(slug, loggedUserId);
  //    return Response.ok(new ArticleResponse(articleData)).status(Response.Status.OK).build();
  //  }

}

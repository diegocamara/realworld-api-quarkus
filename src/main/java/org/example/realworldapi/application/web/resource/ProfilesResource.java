package org.example.realworldapi.application.web.resource;

import lombok.AllArgsConstructor;
import org.example.realworldapi.application.web.model.response.ProfileResponse;
import org.example.realworldapi.domain.feature.FindUserByUsername;
import org.example.realworldapi.domain.feature.FollowUserByUsername;
import org.example.realworldapi.domain.feature.IsFollowingUser;
import org.example.realworldapi.domain.feature.UnfollowUserByUsername;
import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.infrastructure.web.security.annotation.Secured;
import org.example.realworldapi.infrastructure.web.security.profile.Role;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.UUID;

@Path("/profiles")
@AllArgsConstructor
public class ProfilesResource {

  private final FindUserByUsername findUserByUsername;
  private final IsFollowingUser isFollowingUser;
  private final FollowUserByUsername followUserByUsername;
  private final UnfollowUserByUsername unfollowUserByUsername;

  @GET
  @Secured(optional = true)
  @Path("/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProfile(
      @PathParam("username") @NotBlank(message = ValidationMessages.USERNAME_MUST_BE_NOT_BLANK)
          String username,
      @Context SecurityContext securityContext) {
    final var loggedUserId = getLoggedUserId(securityContext);
    final var profileResponse = getProfileResponse(username, loggedUserId);
    return Response.ok(profileResponse).status(Response.Status.OK).build();
  }

  @POST
  @Transactional
  @Secured({Role.USER, Role.ADMIN})
  @Path("/{username}/follow")
  @Produces(MediaType.APPLICATION_JSON)
  public Response follow(
      @PathParam("username") @NotBlank(message = ValidationMessages.USERNAME_MUST_BE_NOT_BLANK)
          String username,
      @Context SecurityContext securityContext) {
    final var loggedUserId = getLoggedUserId(securityContext);
    followUserByUsername.handle(loggedUserId, username);
    return Response.ok(getProfileResponse(username, loggedUserId))
        .status(Response.Status.OK)
        .build();
  }

  @DELETE
  @Transactional
  @Secured({Role.USER, Role.ADMIN})
  @Path("/{username}/follow")
  @Produces(MediaType.APPLICATION_JSON)
  public Response unfollow(
      @PathParam("username") @NotBlank(message = ValidationMessages.USERNAME_MUST_BE_NOT_BLANK)
          String username,
      @Context SecurityContext securityContext) {
    final var loggedUserId = getLoggedUserId(securityContext);
    unfollowUserByUsername.handle(loggedUserId, username);
    return Response.ok(getProfileResponse(username, loggedUserId))
        .status(Response.Status.OK)
        .build();
  }

  private ProfileResponse getProfileResponse(String username, UUID loggedUserId) {
    final var user = findUserByUsername.handle(username);
    final var profileResponse = new ProfileResponse(user);
    if (loggedUserId != null) {
      profileResponse.setFollowing(isFollowingUser.handle(loggedUserId, user.getId()));
    }
    return profileResponse;
  }

  private UUID getLoggedUserId(SecurityContext securityContext) {
    Principal principal = securityContext.getUserPrincipal();
    return principal != null ? UUID.fromString(principal.getName()) : null;
  }
}

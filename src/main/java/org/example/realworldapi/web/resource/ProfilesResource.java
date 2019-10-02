package org.example.realworldapi.web.resource;

import org.example.realworldapi.domain.constants.ValidationMessages;
import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.service.ProfilesService;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.web.model.response.ProfileResponse;
import org.example.realworldapi.web.security.annotation.Secured;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/profiles")
public class ProfilesResource {

  private ProfilesService profilesService;

  public ProfilesResource(ProfilesService profilesService) {
    this.profilesService = profilesService;
  }

  @GET
  @Secured(optional = true)
  @Path("/{username}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getProfile(
      @PathParam("username") @NotBlank(message = ValidationMessages.USERNAME_MUST_BE_NOT_BLANK)
          String username,
      @Context SecurityContext securityContext) {

    Long loggedUserId =
        securityContext.getUserPrincipal() != null
            ? Long.valueOf(securityContext.getUserPrincipal().getName())
            : null;

    Profile profile = profilesService.getProfile(username, loggedUserId);

    return Response.ok(new ProfileResponse(profile)).status(Response.Status.OK).build();
  }

  @POST
  @Secured({Role.USER, Role.ADMIN})
  @Path("/{username}/follow")
  @Produces(MediaType.APPLICATION_JSON)
  public Response follow(
      @PathParam("username") @NotBlank(message = ValidationMessages.USERNAME_MUST_BE_NOT_BLANK)
          String username,
      @Context SecurityContext securityContext) {
    Profile profile =
        profilesService.follow(
            Long.valueOf(securityContext.getUserPrincipal().getName()), username);
    return Response.ok(new ProfileResponse(profile)).status(Response.Status.OK).build();
  }

  @DELETE
  @Secured({Role.USER, Role.ADMIN})
  @Path("/{username}/follow")
  @Produces(MediaType.APPLICATION_JSON)
  public Response unfollow(
      @PathParam("username") @NotBlank(message = ValidationMessages.USERNAME_MUST_BE_NOT_BLANK)
          String username,
      @Context SecurityContext securityContext) {
    Profile profile =
        profilesService.unfollow(
            Long.valueOf(securityContext.getUserPrincipal().getName()), username);
    return Response.ok(new ProfileResponse(profile)).status(Response.Status.OK).build();
  }
}

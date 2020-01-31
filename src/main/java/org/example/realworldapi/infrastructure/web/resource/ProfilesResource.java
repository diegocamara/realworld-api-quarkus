package org.example.realworldapi.infrastructure.web.resource;

import org.example.realworldapi.application.data.ProfileData;
import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.domain.service.ProfilesService;
import org.example.realworldapi.infrastructure.web.model.response.ProfileResponse;
import org.example.realworldapi.infrastructure.web.security.annotation.Secured;
import org.example.realworldapi.infrastructure.web.security.profile.Role;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

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
    Long loggedUserId = getLoggedUserId(securityContext);
    ProfileData profile = profilesService.getProfile(username, loggedUserId);
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
    ProfileData profile = profilesService.follow(getLoggedUserId(securityContext), username);
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
    ProfileData profile = profilesService.unfollow(getLoggedUserId(securityContext), username);
    return Response.ok(new ProfileResponse(profile)).status(Response.Status.OK).build();
  }

  private Long getLoggedUserId(SecurityContext securityContext) {
    Principal principal = securityContext.getUserPrincipal();
    return principal != null ? Long.valueOf(principal.getName()) : null;
  }
}

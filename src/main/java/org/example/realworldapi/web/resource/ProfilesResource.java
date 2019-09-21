package org.example.realworldapi.web.resource;

import org.example.realworldapi.domain.entity.Profile;
import org.example.realworldapi.domain.service.ProfilesService;
import org.example.realworldapi.web.dto.ProfileDTO;
import org.example.realworldapi.web.security.annotation.Secured;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
      @PathParam("username") @NotBlank String username, @Context SecurityContext securityContext) {

    Long loggedUserId =
        securityContext.getUserPrincipal() != null
            ? Long.valueOf(securityContext.getUserPrincipal().getName())
            : null;

    Profile profile = profilesService.getProfile(username, loggedUserId);

    return Response.ok(new ProfileDTO(profile)).status(Response.Status.OK).build();
  }
}

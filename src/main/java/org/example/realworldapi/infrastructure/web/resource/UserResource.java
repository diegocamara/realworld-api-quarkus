package org.example.realworldapi.infrastructure.web.resource;

import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.infrastructure.web.security.profile.Role;
import org.example.realworldapi.infrastructure.web.model.request.UpdateUserRequest;
import org.example.realworldapi.infrastructure.web.model.response.UserResponse;
import org.example.realworldapi.infrastructure.web.security.annotation.Secured;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/user")
public class UserResource {

  private UsersService usersService;

  public UserResource(UsersService usersService) {
    this.usersService = usersService;
  }

  @GET
  @Secured({Role.ADMIN, Role.USER})
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@Context SecurityContext securityContext) {
    User user = usersService.findById(Long.valueOf(securityContext.getUserPrincipal().getName()));
    return Response.ok(new UserResponse(user)).status(Response.Status.OK).build();
  }

  @PUT
  @Secured({Role.USER, Role.USER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(
      @Context SecurityContext securityContext,
      @Valid @NotNull(message = ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL)
              UpdateUserRequest updateUserRequest) {
    User updatedUser =
        usersService.update(
            updateUserRequest.toUser(Long.valueOf(securityContext.getUserPrincipal().getName())));
    return Response.ok(new UserResponse(updatedUser)).status(Response.Status.OK).build();
  }
}

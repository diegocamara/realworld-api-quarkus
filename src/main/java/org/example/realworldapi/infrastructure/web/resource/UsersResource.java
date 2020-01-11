package org.example.realworldapi.infrastructure.web.resource;

import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.domain.model.entity.User;
import org.example.realworldapi.domain.model.exception.UserNotFoundException;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.infrastructure.web.model.request.LoginRequest;
import org.example.realworldapi.infrastructure.web.model.request.NewUserRequest;
import org.example.realworldapi.infrastructure.web.model.response.UserResponse;
import org.example.realworldapi.infrastructure.web.exception.UnauthorizedException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
public class UsersResource {

  private UsersService usersService;

  UsersResource(UsersService usersService) {
    this.usersService = usersService;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response create(
      @Valid @NotNull(message = ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL)
              NewUserRequest newUserRequest,
      @Context SecurityException context) {
    User createdUser =
        usersService.create(
            newUserRequest.getUsername(), newUserRequest.getEmail(), newUserRequest.getPassword());
    return Response.ok(new UserResponse(createdUser)).status(Response.Status.CREATED).build();
  }

  @POST
  @Path("/login")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response login(
      @Valid @NotNull(message = ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL)
              LoginRequest loginRequest) {
    User existingUser;
    try {
      existingUser = usersService.login(loginRequest.getEmail(), loginRequest.getPassword());
    } catch (UserNotFoundException ex) {
      throw new UnauthorizedException();
    }
    return Response.ok(new UserResponse(existingUser)).status(Response.Status.OK).build();
  }
}

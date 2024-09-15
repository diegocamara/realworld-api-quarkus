package org.example.realworldapi.application.web.resource;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.example.realworldapi.application.web.model.request.UpdateUserRequest;
import org.example.realworldapi.application.web.model.response.UserResponse;
import org.example.realworldapi.domain.feature.FindUserById;
import org.example.realworldapi.domain.feature.UpdateUser;
import org.example.realworldapi.domain.model.constants.ValidationMessages;
import org.example.realworldapi.infrastructure.web.provider.TokenProvider;
import org.example.realworldapi.infrastructure.web.security.annotation.Secured;
import org.example.realworldapi.infrastructure.web.security.profile.Role;

@Path("/user")
@AllArgsConstructor
public class UserResource {

  private final FindUserById findUserById;
  private final UpdateUser updateUser;
  private final TokenProvider tokenProvider;

  @GET
  @Secured({Role.ADMIN, Role.USER})
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@Context SecurityContext securityContext) {
    final var userId = UUID.fromString(securityContext.getUserPrincipal().getName());
    final var user = findUserById.handle(userId);
    final var token = tokenProvider.createUserToken(user.getId().toString());
    return Response.ok(new UserResponse(user, token)).status(Response.Status.OK).build();
  }

  @PUT
  @Transactional
  @Secured({Role.ADMIN, Role.USER})
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(
      @Context SecurityContext securityContext,
      @Valid @NotNull(message = ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL)
          UpdateUserRequest updateUserRequest) {
    final var userId = UUID.fromString(securityContext.getUserPrincipal().getName());
    final var user = updateUser.handle(updateUserRequest.toUpdateUserInput(userId));
    final var token = tokenProvider.createUserToken(user.getId().toString());
    return Response.ok(new UserResponse(user, token)).status(Response.Status.OK).build();
  }
}

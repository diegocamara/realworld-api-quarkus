package org.example.realworldapi.web.resource;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.web.exception.ResourceNotFoundException;
import org.example.realworldapi.web.exception.UnauthorizedException;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.infrastructure.annotation.Secured;
import org.example.realworldapi.web.dto.LoginDTO;
import org.example.realworldapi.web.dto.NewUserDTO;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/users")
@RequestScoped
public class UsersResource {

    private UsersService usersService;

    UsersResource(UsersService usersService) {
        this.usersService = usersService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Valid NewUserDTO newUserDTO, @Context SecurityException context) {
        User createdUser = usersService.create(newUserDTO.getUsername(), newUserDTO.getEmail(), newUserDTO.getPassword());
        return Response.ok(createdUser).status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginDTO loginDTO) {
        User existingUser = usersService.login(loginDTO.getEmail(), loginDTO.getPassword())
                .orElseThrow(UnauthorizedException::new);
        return Response.ok(existingUser).status(200).build();
    }

    @GET
    @Secured(Role.USER)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@Context SecurityContext securityContext){
        User user = usersService.findById(Long.valueOf(securityContext.getUserPrincipal().getName()))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return Response.ok(user).status(200).build();
    }
}
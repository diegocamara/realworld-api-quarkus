package org.example.realworldapi.web.resource;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.web.dto.LoginDTO;
import org.example.realworldapi.web.dto.NewUserDTO;

import javax.validation.Valid;
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
    public Response create(@Valid NewUserDTO newUserDTO, @Context SecurityException context) {
        User createdUser = usersService.create(newUserDTO.getUsername(), newUserDTO.getEmail(), newUserDTO.getPassword());
        return Response.ok(createdUser).status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginDTO loginDTO) {
        User existingUser = usersService.login(loginDTO.getEmail(), loginDTO.getPassword());
        return Response.ok(existingUser).status(200).build();
    }

}
package org.example.realworldapi.web.resource;

import org.example.realworldapi.domain.entity.User;
import org.example.realworldapi.domain.service.UsersService;
import org.example.realworldapi.web.dto.NewUserDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/users")
public class UsersResource {

    private UsersService usersService;

    UsersResource(UsersService usersService) {
        usersService = usersService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(NewUserDTO newUserDTO) {
        return Response.ok().status(Response.Status.CREATED).build();
    }

}
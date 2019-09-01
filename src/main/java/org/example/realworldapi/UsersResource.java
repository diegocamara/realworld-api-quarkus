package org.example.realworldapi;

import org.example.realworldapi.domain.User;
import org.example.realworldapi.dto.NewUserDTO;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("/users")
public class UsersResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(NewUserDTO newUserDTO) {

        User user = new User();
        user.setToken(UUID.randomUUID().toString());

        return Response.ok(user).status(Response.Status.CREATED).build();
    }

}
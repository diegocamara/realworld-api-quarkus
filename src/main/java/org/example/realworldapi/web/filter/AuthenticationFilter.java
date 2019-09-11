package org.example.realworldapi.web.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.realworldapi.web.exception.UnauthorizedException;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.infrastructure.annotation.Secured;
import org.example.realworldapi.web.util.JWTUtils;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private final String AUTHORIZATION_HEADER_PREFIX = "Token ";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            throw new UnauthorizedException();
        }

        String token = authorizationHeader.replace(AUTHORIZATION_HEADER_PREFIX, "");

        try {

            DecodedJWT decodedJWT = JWTUtils.verify(token);

            containerRequestContext.setSecurityContext(securityContext(decodedJWT));

        } catch (JWTVerificationException ex) {
            containerRequestContext.abortWith(Response.ok(Response.Status.UNAUTHORIZED.toString()).status(Response.Status.UNAUTHORIZED).build());
        }

    }

    private SecurityContext securityContext(DecodedJWT decodedJWT) {
        return new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return decodedJWT::getSubject;
            }
            @Override
            public boolean isUserInRole(String role) {
                Role[] tokenRoles = JWTUtils.extractRoles(decodedJWT);
                for(Role tokenRole: tokenRoles){
                    if(role.equals(tokenRole.name())){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public String getAuthenticationScheme() {
                return null;
            }
        };
    }
}
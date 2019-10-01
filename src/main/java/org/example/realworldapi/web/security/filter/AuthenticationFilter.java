package org.example.realworldapi.web.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.domain.security.service.JWTService;
import org.example.realworldapi.web.exception.UnauthorizedException;
import org.example.realworldapi.web.security.annotation.Secured;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

  private final String AUTHORIZATION_HEADER_PREFIX = "Token ";

  @Inject JWTService jwtService;

  @Context private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {

    String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

    if (authorizationHeader != null
        && authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {

      String token = authorizationHeader.replace(AUTHORIZATION_HEADER_PREFIX, "");

      try {

        DecodedJWT decodedJWT = jwtService.verify(token);

        containerRequestContext.setSecurityContext(securityContext(decodedJWT));

      } catch (JWTVerificationException ex) {
        containerRequestContext.abortWith(
            Response.ok(Response.Status.UNAUTHORIZED.toString())
                .status(Response.Status.UNAUTHORIZED)
                .build());
      }
    } else {

      if (!isSecurityOptional()) {
        throw new UnauthorizedException();
      }
    }
  }

  private boolean isSecurityOptional() {

    Class<?> resourceClass = resourceInfo.getResourceClass();
    Method resourceMethod = resourceInfo.getResourceMethod();
    Secured resourceClassSecured = resourceClass.getAnnotation(Secured.class);
    Secured resourceMethodSecured = resourceMethod.getAnnotation(Secured.class);

    if (resourceMethodSecured != null) {
      return resourceMethodSecured.optional();
    }

    if (resourceClassSecured != null) {
      return resourceClassSecured.optional();
    }

    return false;
  }

  private SecurityContext securityContext(DecodedJWT decodedJWT) {
    return new SecurityContext() {
      @Override
      public Principal getUserPrincipal() {
        return decodedJWT::getSubject;
      }

      @Override
      public boolean isUserInRole(String role) {
        Role[] tokenRoles = jwtService.extractRoles(decodedJWT);
        for (Role tokenRole : tokenRoles) {
          if (role.equals(tokenRole.name())) {
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

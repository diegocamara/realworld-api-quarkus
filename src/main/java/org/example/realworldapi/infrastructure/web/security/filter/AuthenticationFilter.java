package org.example.realworldapi.infrastructure.web.security.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.realworldapi.domain.model.provider.TokenProvider;
import org.example.realworldapi.infrastructure.web.exception.UnauthorizedException;
import org.example.realworldapi.infrastructure.web.security.annotation.Secured;
import org.example.realworldapi.infrastructure.web.security.context.DecodedJWTSecurityContext;
import org.example.realworldapi.infrastructure.web.security.context.EmptySecurityContext;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

  private final String AUTHORIZATION_HEADER_PREFIX = "Token ";

  @Inject
  TokenProvider tokenProvider;

  @Context private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {

    String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

    if (authorizationHeader != null
        && authorizationHeader.startsWith(AUTHORIZATION_HEADER_PREFIX)) {

      String token = authorizationHeader.replace(AUTHORIZATION_HEADER_PREFIX, "");

      try {

        DecodedJWT decodedJWT = tokenProvider.verify(token);

        containerRequestContext.setSecurityContext(
            new DecodedJWTSecurityContext(decodedJWT, tokenProvider));

      } catch (JWTVerificationException ex) {
        containerRequestContext.abortWith(
            Response.ok(Response.Status.UNAUTHORIZED.toString())
                .status(Response.Status.UNAUTHORIZED)
                .build());
      }
    } else {

      containerRequestContext.setSecurityContext(new EmptySecurityContext());

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
}

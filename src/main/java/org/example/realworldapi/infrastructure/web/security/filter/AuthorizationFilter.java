package org.example.realworldapi.infrastructure.web.security.filter;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.example.realworldapi.infrastructure.web.exception.ForbiddenException;
import org.example.realworldapi.infrastructure.web.security.annotation.Secured;
import org.example.realworldapi.infrastructure.web.security.profile.Role;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

  @Context private ResourceInfo resourceInfo;

  @Override
  public void filter(ContainerRequestContext containerRequestContext) throws IOException {

    Class<?> resourceClass = resourceInfo.getResourceClass();
    Method resourceMethod = resourceInfo.getResourceMethod();

    if (!isSecurityOptional(resourceMethod, resourceClass)) {
      SecurityContext securityContext = containerRequestContext.getSecurityContext();

      List<Role> classRoles = extractRoles(resourceClass);
      List<Role> methodRoles = extractRoles(resourceMethod);

      try {
        if (methodRoles.isEmpty()) {
          checkPermissions(classRoles, securityContext);
        } else {
          checkPermissions(methodRoles, securityContext);
        }
      } catch (Exception ex) {
        containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
      }
    }
  }

  private boolean isSecurityOptional(Method resourceMethod, Class<?> resourceClass) {
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

  private void checkPermissions(List<Role> allowedRoles, SecurityContext securityContext) {
    if (!isAccessAllowed(allowedRoles, securityContext)) {
      throw new ForbiddenException();
    }
  }

  private boolean isAccessAllowed(List<Role> allowedRoles, SecurityContext securityContext) {
    for (Role allowedRole : allowedRoles) {
      if (securityContext.isUserInRole(allowedRole.name())) {
        return true;
      }
    }
    return false;
  }

  private List<Role> extractRoles(AnnotatedElement annotatedElement) {
    if (annotatedElement == null) {
      return new LinkedList<>();
    } else {
      Secured secured = annotatedElement.getAnnotation(Secured.class);
      if (secured == null) {
        return new LinkedList<>();
      } else {
        return Arrays.asList(secured.value());
      }
    }
  }
}

package org.example.realworldapi.web.security.filter;

import org.example.realworldapi.web.exception.ForbiddenException;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.web.security.annotation.Secured;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        SecurityContext securityContext = containerRequestContext.getSecurityContext();

        Class<?> resourceClass = resourceInfo.getResourceClass();
        List<Role> classRoles = extractRoles(resourceClass);
        Method resourceMethod = resourceInfo.getResourceMethod();
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
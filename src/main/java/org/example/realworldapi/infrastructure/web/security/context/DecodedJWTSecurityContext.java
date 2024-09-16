package org.example.realworldapi.infrastructure.web.security.context;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.ws.rs.core.SecurityContext;
import java.security.Principal;
import org.example.realworldapi.infrastructure.web.provider.TokenProvider;
import org.example.realworldapi.infrastructure.web.security.profile.Role;

public class DecodedJWTSecurityContext implements SecurityContext {

  private final DecodedJWT decodedJWT;
  private final TokenProvider tokenProvider;

  public DecodedJWTSecurityContext(DecodedJWT decodedJWT, TokenProvider tokenProvider) {
    this.decodedJWT = decodedJWT;
    this.tokenProvider = tokenProvider;
  }

  @Override
  public Principal getUserPrincipal() {
    return decodedJWT::getSubject;
  }

  @Override
  public boolean isUserInRole(String role) {
    Role[] tokenRoles = tokenProvider.extractRoles(decodedJWT);
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
}

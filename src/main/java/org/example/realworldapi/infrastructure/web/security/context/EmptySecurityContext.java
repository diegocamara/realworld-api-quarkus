package org.example.realworldapi.infrastructure.web.security.context;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class EmptySecurityContext implements SecurityContext {
  @Override
  public Principal getUserPrincipal() {
    return null;
  }

  @Override
  public boolean isUserInRole(String s) {
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

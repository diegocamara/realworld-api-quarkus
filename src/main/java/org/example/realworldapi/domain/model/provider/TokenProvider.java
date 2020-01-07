package org.example.realworldapi.domain.model.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.realworldapi.infrastructure.web.security.profile.Role;

public interface TokenProvider {

  String createUserToken(String subject);

  DecodedJWT verify(String token);

  Role[] extractRoles(DecodedJWT decodedJWT);
}

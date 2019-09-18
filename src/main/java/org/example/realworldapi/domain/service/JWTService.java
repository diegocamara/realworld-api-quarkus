package org.example.realworldapi.domain.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.realworldapi.domain.security.Role;

public interface JWTService {

    String sign(String subject, Role... allowedRoles);

    DecodedJWT verify(String token);

    Role[] extractRoles(DecodedJWT decodedJWT);

}

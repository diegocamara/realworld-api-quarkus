package org.example.realworldapi.web.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.realworldapi.domain.security.Role;

import java.util.Calendar;
import java.util.Date;

public class JWTUtils {

    public static final String CLAIM_ROLES = "ROLES";
    private static ObjectMapper objectMapper;
    private static Algorithm algorithm;
    private static JWTVerifier jwtVerifier;
    private static String issuer = "users-service";

    static {
        objectMapper = new ObjectMapper();
        algorithm = Algorithm.HMAC256("secret123");
        jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
    }

    public static String sign(Object subject, Integer expirationTimeInMinutes, Role... allowedRoles) {
        JWTCreator.Builder builder;
        try {
            String subjectValue = objectMapper.writeValueAsString(subject);
            builder = JWT.create().withIssuer(issuer).withSubject(subjectValue);

            if (allowedRoles.length > 0) {
                builder.withArrayClaim(CLAIM_ROLES, toArrayNames(allowedRoles));
            }

            if (expirationTimeInMinutes != null) {
                builder.withExpiresAt(plusMinutes(expirationTimeInMinutes));
            }
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

        return builder.sign(algorithm);
    }

    public static DecodedJWT verify(String token) {
        return jwtVerifier.verify(token);
    }

    public static Role[] extractRoles(DecodedJWT decodedJWT){
        Claim claim = decodedJWT.getClaim(CLAIM_ROLES);
        return claim.asArray(Role.class);
    }

    private static String[] toArrayNames(Role... allowedRoles) {

        String[] names = new String[allowedRoles.length];

        for (int nameIndex = 0; nameIndex < allowedRoles.length; nameIndex++) {
            names[nameIndex] = allowedRoles[nameIndex].name();
        }

        return names;
    }

    private static Date plusMinutes(int minutes) {
        int oneMinuteInMillis = 60000;
        Calendar calendar = Calendar.getInstance();
        return new Date(calendar.getTimeInMillis() + (minutes * oneMinuteInMillis));
    }

}
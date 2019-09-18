package org.example.realworldapi.domain.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.example.realworldapi.domain.security.Role;
import org.example.realworldapi.domain.service.JWTService;

import javax.enterprise.context.ApplicationScoped;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class JWTServiceImpl implements JWTService {

    public final String COMPLEMENTARY_SUBSCRIPTION = "complementary-subscription";
    public final String CLAIM_ROLES = "ROLES";
    private Algorithm algorithm;
    private JWTVerifier jwtVerifier;
    private String issuer;
    private Integer expirationTimeInMinutes;

    public JWTServiceImpl(@ConfigProperty(name = "jwt.issuer") String issuer,
                          @ConfigProperty(name = "jwt.secret") String secret,
                          @ConfigProperty(name = "jwt.expiration.time.minutes") Integer expirationTimeInMinutes){

        this.issuer = issuer;
        this.algorithm = Algorithm.HMAC512(secret);
        this.jwtVerifier = JWT.require(algorithm).withIssuer(issuer).build();
        this.expirationTimeInMinutes = expirationTimeInMinutes;

    }

    @Override
    public String sign(String subject, Role... allowedRoles) {
        JWTCreator.Builder builder;

        builder = JWT.create().withIssuer(issuer).withSubject(subject)
                .withIssuedAt(new Date())
                .withClaim(COMPLEMENTARY_SUBSCRIPTION, UUID.randomUUID().toString());

        if (allowedRoles.length > 0) {
            builder.withArrayClaim(CLAIM_ROLES, toArrayNames(allowedRoles));
        }

        if (expirationTimeInMinutes != null) {
            builder.withExpiresAt(plusMinutes(expirationTimeInMinutes));
        }


        return builder.sign(algorithm);
    }

    @Override
    public DecodedJWT verify(String token) {
        return jwtVerifier.verify(token);
    }

    @Override
    public Role[] extractRoles(DecodedJWT decodedJWT) {
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